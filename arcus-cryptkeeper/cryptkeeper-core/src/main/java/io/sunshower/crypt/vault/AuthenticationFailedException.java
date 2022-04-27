package io.sunshower.crypt.vault;

import io.sunshower.crypt.core.VaultException;

public class AuthenticationFailedException extends VaultException {

  public AuthenticationFailedException(String s) {
    super(s);
  }
}
