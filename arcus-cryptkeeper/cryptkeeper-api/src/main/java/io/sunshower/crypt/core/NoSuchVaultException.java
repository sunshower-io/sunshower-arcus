package io.sunshower.crypt.core;

public class NoSuchVaultException extends VaultException {

  public NoSuchVaultException(String formatted) {
    super(formatted);
  }
}
