package io.sunshower.crypt;


import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sunshower.crypt.core.LeaseRequest;
import io.sunshower.crypt.core.Leases;
import io.sunshower.crypt.core.Secret;
import io.sunshower.crypt.core.SecretService;
import io.sunshower.crypt.core.Vault;
import io.sunshower.crypt.secrets.StringSecret;
import io.sunshower.crypt.vault.DefaultVault;
import java.io.File;
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
  void ensureAddingVaultWorks() {
    val id = service.add(vault, "hello");
    val lease = service.lease(id, leaseRequest);
    assertTrue(lease.list().isEmpty());
    Thread.sleep(TimeUnit.MILLISECONDS.toMillis(10));
  }

}