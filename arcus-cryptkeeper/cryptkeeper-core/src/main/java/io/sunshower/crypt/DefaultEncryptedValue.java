package io.sunshower.crypt;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.crypt.core.EncryptedValue;
import io.sunshower.lang.common.encodings.Encoding;
import io.sunshower.lang.common.encodings.Encodings;
import io.sunshower.lang.common.encodings.Encodings.Type;
import io.sunshower.persistence.id.Identifier;
import io.sunshower.persistence.id.Identifiers;
import io.sunshower.persistence.id.Sequence;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import lombok.NonNull;

@SuppressFBWarnings
public final class DefaultEncryptedValue implements EncryptedValue {

  static final Sequence<Identifier> IDENTIFIER_SEQUENCE = Identifiers.newSequence(true);
  private final String salt;
  private final Encoding encoding;
  private final InputStream source;
  private final String initializationVector;
  private Identifier id;
  private String name;
  private String description;

  public DefaultEncryptedValue(
      @NonNull InputStream inputStream,
      @NonNull byte[] salt,
      @NonNull byte[] initializationVector) {
    this(Encodings.create(Type.Base58), inputStream, salt, initializationVector);
  }

  public DefaultEncryptedValue(
      @NonNull Encoding encoding,
      @NonNull InputStream source,
      @NonNull byte[] salt,
      @NonNull byte[] initializationVector) {
    this.id = IDENTIFIER_SEQUENCE.next();
    this.source = source;
    this.encoding = encoding;
    this.salt = encoding.encode(salt);
    this.initializationVector = encoding.encode(initializationVector);
  }

  public DefaultEncryptedValue(
      @NonNull Encoding encoding,
      @NonNull String value,
      @NonNull byte[] salt,
      @NonNull byte[] initializationVector) {
    setId(IDENTIFIER_SEQUENCE.next());
    this.encoding = encoding;
    this.salt = encoding.encode(salt);
    this.initializationVector = encoding.encode(initializationVector);
    this.source = new ByteArrayInputStream(value.getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Identifier getId() {
    return id;
  }

  public void setId(Identifier next) {
    this.id = next;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public Encoding getEncoding() {
    return encoding;
  }

  @Override
  public void readCipherText(OutputStream outputStream) throws IOException {
    if (source.markSupported()) {
      source.reset();
    }
    source.transferTo(outputStream);
  }

  @Override
  public CharSequence getSalt() {
    return salt;
  }

  @Override
  public CharSequence getInitializationVector() {
    return initializationVector;
  }
}
