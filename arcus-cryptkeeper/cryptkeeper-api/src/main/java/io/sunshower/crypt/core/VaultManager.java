package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;
import java.util.Collection;
import java.util.List;

public interface VaultManager extends AutoCloseable {

  Vault unlock(Identifier id, CharSequence password);

  void lock(Vault vault);

  List<Vault> getOpenVaults();

  Vault addVault(Vault vault, CharSequence password);

  Vault addSecret(Vault vault, Secret secret);

  Vault addSecrets(Vault vault, Collection<? extends Secret> secrets);

  EncryptedValue encrypt(Vault owner, Secret secret);

  <T extends Secret> T decrypt(Vault owner, EncryptedValue encryptedValue);

  Vault flush(Vault vault);

  boolean deleteVault(Vault vault, CharSequence password);
}
