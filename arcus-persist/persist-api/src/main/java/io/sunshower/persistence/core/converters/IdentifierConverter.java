package io.sunshower.persistence.core.converters;

import io.sunshower.common.Identifier;
import io.sunshower.common.Identifiers;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class IdentifierConverter implements AttributeConverter<Identifier, byte[]> {
  @Override
  public byte[] convertToDatabaseColumn(Identifier identifier) {
    if (identifier == null) {
      return null;
    }
    return Identifiers.getBytes(identifier);
  }

  @Override
  public Identifier convertToEntityAttribute(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    return Identifier.valueOf(bytes);
  }
}
