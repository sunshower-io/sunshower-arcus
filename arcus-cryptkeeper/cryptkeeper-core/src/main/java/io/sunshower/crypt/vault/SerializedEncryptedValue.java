package io.sunshower.crypt.vault;

import io.sunshower.arcus.condensation.Alias;
import io.sunshower.arcus.condensation.Attribute;
import io.sunshower.arcus.condensation.Element;
import io.sunshower.arcus.condensation.RootElement;
import io.sunshower.crypt.core.EncryptedValue;
import io.sunshower.lang.common.encodings.Encoding;
import io.sunshower.lang.common.encodings.Encodings;
import io.sunshower.lang.common.encodings.Encodings.Type;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.val;

@RootElement
public class SerializedEncryptedValue implements EncryptedValue {

  @Attribute private String name;

  @Attribute private String description;

  @Element(alias = @Alias(read = "cipher-text", write = "cipher-text"))
  private String cypherText;

  @Element private String salt;

  @Element(alias = @Alias(read = "initialization-vector", write = "initialization-vector"))
  private String initializationVector;

  @SneakyThrows
  public SerializedEncryptedValue(EncryptedValue encryptedValue) {
    setName(encryptedValue.getName());
    setDescription(encryptedValue.getDescription());
    this.salt = encryptedValue.getSalt().toString();
    this.initializationVector = encryptedValue.getInitializationVector().toString();
    this.cypherText = encryptedValue.getCipherText().toString();
  }

  public SerializedEncryptedValue() {}

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void setName(String name) {
    this.name = name;
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
    return Encodings.create(Type.Base58);
  }

  @Override
  public void readCipherText(OutputStream outputStream) throws IOException {
    val encoded = getEncoding().encode(cypherText.getBytes(StandardCharsets.UTF_8));
    outputStream.write(encoded.getBytes(StandardCharsets.UTF_8));
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
