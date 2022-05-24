package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;
import io.sunshower.persistence.id.Identifiers;
import java.util.Collection;
import java.util.List;

public interface VaultManager extends AutoCloseable {

  void close();

  Vault unlock(Identifier id, CharSequence password);

  void setPassword(Identifier id, CharSequence oldPassword, CharSequence newPassword);

  void lock(Vault vault);

  List<Vault> getOpenVaults();

  Vault createVault(
      String name, String description, Identifier id, CharSequence password, byte[] salt,
      byte[] iv);


  default Vault createVault(
      String name, String description, CharSequence password, byte[] salt,
      byte[] iv) {
    return createVault(name, description, Identifiers.newSequence().next(), password, salt, iv);
  }

  Vault addVault(Vault vault, CharSequence password);


  Vault addSecret(Vault vault, Secret secret);

  Vault addSecrets(Vault vault, Collection<? extends Secret> secrets);

  EncryptedValue encrypt(Vault owner, Secret secret);

  <T extends Secret> T decrypt(Vault owner, EncryptedValue encryptedValue);

  Vault flush(Vault vault);

  boolean deleteVault(Vault vault, CharSequence password);

  EncryptionServiceSet createEncryptionServiceSet();

  EncryptionServiceSet createEncryptionServiceSet(CharSequence password);

  Vault createDefaultVault(String name, String description, CharSequence password);
}
