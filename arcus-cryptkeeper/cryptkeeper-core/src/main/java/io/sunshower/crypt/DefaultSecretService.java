package io.sunshower.crypt;

import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.crypt.core.EncryptionServiceFactory;
import io.sunshower.crypt.core.Lease;
import io.sunshower.crypt.core.LeaseExpiredException;
import io.sunshower.crypt.core.LeaseReaper;
import io.sunshower.crypt.core.LeaseRequest;
import io.sunshower.crypt.core.Secret;
import io.sunshower.crypt.core.SecretLease;
import io.sunshower.crypt.core.SecretService;
import io.sunshower.crypt.core.Vault;
import io.sunshower.crypt.core.VaultLease;
import io.sunshower.crypt.core.VaultManager;
import io.sunshower.crypt.vault.FileBackedVaultManager;
import io.sunshower.persistence.id.Identifier;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;

public class DefaultSecretService implements SecretService {

  private final LeaseReaper leaseReaper;
  private final VaultManager vaultManager;


  private final Map<Identifier, Vault> openVaults;


  protected DefaultSecretService(@NonNull VaultManager vaultManager) {
    this.vaultManager = vaultManager;
    this.openVaults = new HashMap<>();
    this.leaseReaper = new ConcurrentLeaseReaper(this);
  }


  public DefaultSecretService(@NonNull final File file, @NonNull Condensation condensation, @NonNull
      EncryptionServiceFactory factory) {
    this(new FileBackedVaultManager(file, condensation, factory));
  }


  public DefaultSecretService(File file, Condensation condensation) {
    this(file, condensation, new DefaultEncryptionServiceFactory());
  }


  @Override
  public VaultLease lease(Identifier identifier, LeaseRequest request) {
    synchronized (openVaults) {
      val vault = vaultManager.unlock(identifier, request.password());
      return new VaultLease() {
        @Override
        public List<Secret> list() {
          return vault.getSecrets().stream().map(secret -> {
            return new Secret() {
              @Override
              public CharSequence getName() {
                return null;
              }

              @Override
              public CharSequence getDescription() {
                return null;
              }

              @Override
              public Identifier getId() {
                return secret.getId();
              }
            };
          }).collect(Collectors.toList());
        }

        @Override
        public Lease<Secret> lease(Identifier id) {
          return null;
        }

        @Override
        public void close(SecretLease lease) {

        }

        @Override
        public boolean save(@NonNull Secret secret) {
          return false;
        }

        @Override
        public boolean delete(Identifier identifier) {
          return false;
        }

        @Override
        public Date getExpiration() {
          return null;
        }

        @Override
        public Date getLeaseDate() {
          return null;
        }

        @Override
        public Vault get() throws LeaseExpiredException {
          return null;
        }

        @Override
        public void close() throws Exception {

        }
      };
    }
  }
}
