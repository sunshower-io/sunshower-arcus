package io.sunshower.crypt.vault;

public class NoSuchVaultException extends VaultException {

  public NoSuchVaultException(String formatted) {
    super(formatted);
  }
}
