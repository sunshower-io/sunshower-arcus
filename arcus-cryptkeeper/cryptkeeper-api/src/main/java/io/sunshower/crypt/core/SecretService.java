package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;

public interface SecretService {


  /**
   *
   * @param identifier the identifier of the vault to lease
   * @return
   */
  VaultLease lease(Identifier identifier);


  boolean close(Lease<Vault> lease);

}
