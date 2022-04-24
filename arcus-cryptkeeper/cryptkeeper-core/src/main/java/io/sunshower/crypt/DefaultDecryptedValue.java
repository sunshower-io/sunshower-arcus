package io.sunshower.crypt;

import io.sunshower.crypt.core.DecryptedValue;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public final class DefaultDecryptedValue implements DecryptedValue {

  private final CharSequence plaintext;

  public DefaultDecryptedValue(CharSequence plaintext) {
    this.plaintext = plaintext;
  }

  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(plaintext.toString().getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public void write(OutputStream outputStream) throws IOException {
    getInputStream().transferTo(outputStream);
  }
}
