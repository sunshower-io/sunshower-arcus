package io.sunshower.crypt.core;

import io.sunshower.lang.primitives.Rope;
import java.io.Serializable;
import java.util.function.Function;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public interface EncryptionService {

  CharSequence encrypt(CharSequence input, Function<byte[], CharSequence> transformer);

  CharSequence decrypt(CharSequence input, Function<byte[], CharSequence> transformer);

  <T extends Serializable> SealedObject encrypt(T object);

  <T extends Serializable> T decrypt(SealedObject object);

  <T extends Serializable> byte[] encryptToBytes(T object);

  <T extends Serializable> T decryptFromBytes(byte[] data);

  EncryptedValue encryptText(CharSequence input);

  DecryptedValue decryptText(EncryptedValue encryptedValue);

  default CharSequence encrypt(CharSequence input) {
    return encrypt(input, Rope::new);
  }

  default CharSequence decrypt(CharSequence input) {
    return decrypt(input, Rope::new);
  }

  SecretKey generatePassword(CharSequence input);
}
