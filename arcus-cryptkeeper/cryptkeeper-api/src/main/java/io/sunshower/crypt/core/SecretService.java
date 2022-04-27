package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;

public interface SecretService {

  /**
   * @param identifier the identifier of the vault to lease
   * @return the lease
   */
  VaultLease lease(Identifier identifier, LeaseRequest request);

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
