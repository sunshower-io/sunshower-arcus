package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;
import java.util.List;

public interface VaultLease extends Lease<Vault> {

  List<Secret> list();

  Lease<Secret> lease(Identifier id);

}
