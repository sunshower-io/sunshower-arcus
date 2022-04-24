package io.sunshower.crypt.core;

import io.sunshower.lang.primitives.Rope;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lombok.val;

public interface DecryptedValue {

  InputStream getInputStream();

  void write(OutputStream outputStream) throws IOException;

  default CharSequence getValue() throws IOException {
    val outputStream = new ByteArrayOutputStream();
    write(outputStream);
    return new Rope(outputStream.toByteArray());
  }
}
