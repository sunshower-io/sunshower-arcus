package io.sunshower.crypt.vault;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.crypt.JCAPasswordEncryptionService;
import io.sunshower.crypt.secrets.StringSecret;
import io.sunshower.lang.primitives.Rope;
import io.sunshower.test.common.Tests;
import java.io.File;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileBackedVaultManagerTest {

  private File root;
  private FileBackedVaultManager manager;
  private DefaultVault vault;
  private Rope password;
  private String material;

  @BeforeEach
  void setUp() {
    root = Tests.createTemp();
    manager =
        new FileBackedVaultManager(
            root,
            Condensation.create("json"),
            (salt, initializationVector, password, encoding) -> {
              val service = new JCAPasswordEncryptionService(encoding, salt, password);
              service.setInitializationVector(initializationVector);
              return service;
            });

    material = "hello world!";
    password = new Rope("password!");
    vault = new DefaultVault();
    vault.setName("test vault");
    vault.setDescription("just a cool vault!");
  }

  @Test
  void ensureSavingVaultWorks() {
    var result = manager.addVault(vault, password);
    result = manager.unlock(vault.getId(), password);
    result.addSecret(
        new StringSecret("This is a cool secret", "Just a secret with a description", material));
    assertEquals(1, result.getSecrets().size());
  }

  @Test
  @SneakyThrows
  void ensureRetrievingSecretsFromLockedVaultDoesntWork() {
    val v = manager.addVault(vault, password);
    v.close();
    assertThrows(
        LockedVaultException.class,
        () -> {
          v.getSecrets();
        });
  }

  @Test
  void ensureUnlockingVaultWithBadPasswordFails() {
    var result = manager.addVault(vault, password);
    val id =
        result.addSecret(
            new StringSecret(
                "This is a cool secret", "Just a secret with a description", material));
    assertEquals(1, result.getSecrets().size());
    manager.lock(result);
    assertThrows(
        AuthenticationFailedException.class,
        () -> {
          manager.unlock(result.getId(), "bad password");
        });
  }

  @Test
  void ensureLockingAndUnlockingVaultWorks() {
    var result = manager.addVault(vault, password);
    val id =
        result.addSecret(
            new StringSecret(
                "This is a cool secret", "Just a secret with a description", material));
    assertEquals(1, result.getSecrets().size());
    manager.lock(result);
    manager.unlock(result.getId(), password);
    assertEquals(1, result.getSecrets().size());
    val secret = (StringSecret) result.getSecret(id);
    assertEquals(secret.getDescription(), "Just a secret with a description");
    assertEquals(secret.getMaterial(), material);
  }

  @Test
  void ensureEncryptedTextIsEncrypted() {
    var result = manager.addVault(vault, password);
    val id =
        result.addSecret(
            new StringSecret(
                "This is a cool secret", "Just a secret with a description", material));
    assertEquals(1, result.getSecrets().size());
    val secret = (StringSecret) result.getSecret(id);
    assertEquals(secret.getDescription(), "Just a secret with a description");
    assertEquals(secret.getMaterial(), material);
  }
}
