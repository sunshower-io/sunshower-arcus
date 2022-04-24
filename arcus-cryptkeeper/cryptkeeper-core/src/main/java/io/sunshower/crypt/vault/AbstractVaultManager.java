package io.sunshower.crypt.vault;

import io.sunshower.crypt.core.DecryptedValue;
import io.sunshower.crypt.core.EncryptedValue;
import io.sunshower.crypt.core.VaultManager;

public abstract class AbstractVaultManager implements VaultManager {

  protected abstract DecryptedValue decrypt(EncryptedValue secret, CharSequence password);
}
