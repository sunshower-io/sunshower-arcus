package io.sunshower.arcus.persist.jpa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TypeConverter implements AttributeConverter<Class<?>, String> {

  @Override
  public String convertToDatabaseColumn(Class<?> attribute) {
    return attribute == null ? null : attribute.getName();
  }

  @Override
  public Class<?> convertToEntityAttribute(String dbData) {
    try {
      return dbData == null
          ? null
          : Class.forName(dbData, false, Thread.currentThread().getContextClassLoader());
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }
}
