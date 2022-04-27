package io.sunshower.crypt;

import io.sunshower.crypt.core.EncryptionService;
import io.sunshower.crypt.core.EncryptionServiceFactory;
import io.sunshower.lang.common.encodings.Encoding;
import lombok.val;

public class DefaultEncryptionServiceFactory implements EncryptionServiceFactory {

  @Override
  public EncryptionService create(
      CharSequence salt,
      CharSequence initializationVector,
      CharSequence password,
      Encoding encoding) {
    val service = new JCAEncryptionService(encoding, salt, password);
    service.setInitializationVector(initializationVector);
    return service;
  }
}
