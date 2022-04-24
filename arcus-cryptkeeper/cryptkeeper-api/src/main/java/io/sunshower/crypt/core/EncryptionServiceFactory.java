package io.sunshower.crypt.core;

import io.sunshower.lang.common.encodings.Encoding;
import io.sunshower.lang.common.encodings.Encodings;
import io.sunshower.lang.common.encodings.Encodings.Type;

public interface EncryptionServiceFactory {

  EncryptionService create(
      CharSequence salt,
      CharSequence initializationVector,
      CharSequence password,
      Encoding encoding);

  default EncryptionService create(CharSequence salt, CharSequence iv, CharSequence password) {
    return create(salt, iv, password, Encodings.create(Type.Base58));
  }
}
