package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;
import lombok.NonNull;

public interface SecretService extends AutoCloseable {

  /**
   * @param request the lease request
   * @return a collection that honors the lease request
   */
  SecretCollection list(@NonNull Identifier vaultId, @NonNull LeaseRequest request);

  void close();

  /**
   * @param identifier the identifier of the vault to lease
   * @return the lease
   */
  VaultLease lease(Identifier identifier, LeaseRequest request);

  Identifier add(Vault vault, CharSequence password);

  /**
   * @param lease
   * @return true if the lease was successfully closed
   */
  default boolean close(Lease<?> lease) {
    try {
      lease.close();
      return true;
    } catch (Exception ex) {
      return false;
    }
  }
}
