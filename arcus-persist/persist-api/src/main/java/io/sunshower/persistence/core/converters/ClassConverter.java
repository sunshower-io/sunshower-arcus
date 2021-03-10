package io.sunshower.persistence.core.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ClassConverter implements AttributeConverter<Class<?>, String> {
  @Override
  public String convertToDatabaseColumn(Class<?> attribute) {
    return attribute != null ? attribute.getName() : Void.class.getName();
  }

  @Override
  public Class<?> convertToEntityAttribute(String dbData) {
    try {
      return Class.forName(dbData, true, Thread.currentThread().getContextClassLoader());
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
