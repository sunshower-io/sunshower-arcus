package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;
import java.util.List;
import lombok.NonNull;

public interface VaultLease extends Lease<Vault> {

  /** @return a list of secrets */
  List<Secret> list();

  /**
   * @param id
   * @return a lease for the secret with the given identifier
   */
  Lease<Secret> lease(Identifier id);

  /** @param lease the lease to close */
  void close(SecretLease lease);

  Identifier save(@NonNull Secret secret);

  /**
   * @param secret the secret to delete
   * @return true if the secret has been successfully deleted
   */
  default boolean delete(Secret secret) {
    return delete(secret.getId());
  }

  boolean delete(Identifier identifier);
}
