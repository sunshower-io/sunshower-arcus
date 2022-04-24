package io.sunshower.crypt.core;

import io.sunshower.lang.common.encodings.Encoding;
import io.sunshower.lang.primitives.Rope;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import lombok.val;

public interface EncryptedValue {

  void setName(String name);

  String getName();

  void setDescription(String description);

  String getDescription();

  /** @return the text encoding used to encode the encrypted value */
  Encoding getEncoding();

  default CharSequence getCipherText() throws IOException {
    val outputStream = new ByteArrayOutputStream();
    readCipherText(outputStream);
    return new Rope(outputStream.toByteArray());
  }

  /**
   * read this encrypted value into the provided outputstream
   *
   * @param outputStream
   */
  void readCipherText(OutputStream outputStream) throws IOException;

  /** @return the salt used to generate this encrypted value */
  CharSequence getSalt();

  /** @return the initialization vector used to create this encrypted value */
  CharSequence getInitializationVector();
}
