package io.sunshower.crypt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sunshower.crypt.core.LeaseRequest;
import io.sunshower.crypt.core.Leases;
import io.sunshower.crypt.core.LockedVaultException;
import io.sunshower.crypt.core.Secret;
import io.sunshower.crypt.core.SecretService;
import io.sunshower.crypt.core.Vault;
import io.sunshower.crypt.secrets.StringSecret;
import io.sunshower.crypt.vault.DefaultVault;
import io.sunshower.persistence.id.Identifier;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class DefaultSecretServiceTest {

  private Vault vault;
  private Secret secret;
  private SecretService service;
  private LeaseRequest leaseRequest;

  @BeforeEach
  void setUp(@TempDir File tempdir) {
    service = new DefaultSecretService(tempdir);
    secret = new StringSecret("hello", "world", "material");
    leaseRequest = Leases.forPassword("password!").expiresIn(1, TimeUnit.MILLISECONDS);
    vault = new DefaultVault("test vault", "just the coolest vault");
  }

  @AfterEach
  void tearDown() {
    service.close();
  }

  @Test
  @SneakyThrows
  void ensureAddingMultipleVaultsWorks() {
    val ids = new ArrayList<Identifier>();
    for (int i = 0; i < 5; i++) {
      vault = new DefaultVault("test vault" + i, "just the coolest vault");
      ids.add(service.add(vault, "password!"));
      val vault =
          service.lease(ids.get(i), Leases.forPassword("password!").expiresIn(100, TimeUnit.DAYS));
      vault.save(secret);
      assertEquals(1, vault.list().size());
    }
  }

  @Test
  @SneakyThrows
  void ensureAddingVaultWorks() {
    val id = service.add(vault, "password!");
    val lease = service.lease(id, leaseRequest);
    assertTrue(lease.list().isEmpty());
    Thread.sleep(1000);
    assertThrows(LockedVaultException.class, lease::list);
  }

  @Test
  void ensureAttemptingToLeaseSecretFromClosedVaultFails() {
    val vault =
        service.createVault(
            "test vault",
            "test vault",
            Leases.forPassword("password").expiresIn(100, TimeUnit.SECONDS));
    val secret = vault.save(new StringSecret("hello", "world", "how are you?"));
    vault.close();
    assertThrows(
        LockedVaultException.class,
        () -> {
          vault.lease(secret);
        });
  }

  @Test
  void ensureCreatingDefaultVaultWorks() {
    val set = service.createEncryptionServiceSet("password");
    val manager = service.getVaultManager();

    var vault =
        manager.createVault(
            "test vault", "test vault", "password", set.getSalt(), set.getInitializationVector());

    vault.getSecrets();
    vault.close();
    assertThrows(LockedVaultException.class, vault::getSecrets);
    vault = manager.unlock(vault.getId(), "password");
    vault.getSecrets();
  }
}
