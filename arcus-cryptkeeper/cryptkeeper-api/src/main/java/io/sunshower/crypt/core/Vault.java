package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;
import java.util.List;

public interface Vault extends AutoCloseable {

  String getName();

  void setName(String name);

  String getDescription();

  void setDescription(String description);

  Identifier getId();

  VaultManager getManager();

  DecryptedValue open(Identifier id);

  List<EncryptedValue> getSecrets();

  String getIcon();
  void setIcon(String icon);

  Identifier addSecret(Secret secret);


  boolean deleteSecret(Identifier id);

  <T extends Secret> T getSecret(Identifier id);

}
