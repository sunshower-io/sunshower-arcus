package io.sunshower.crypt.core;

public interface EncryptionServiceSet {

  /** @return the salt that this encryption service was created with */
  byte[] getSalt();

  /** @return the initialization vector this encryption service was created with */
  byte[] getInitializationVector();

  /** @return the unencoded password this encryption service was created with */
  byte[] getPassword();

  /**
   * @return a transient encryption service initialized with the salt, initialization vector, and
   *     password returned by the relevant methods on this class
   */
  EncryptionService getEncryptionService();
}
