package io.sunshower.persistence.core.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class StringArrayConverter implements AttributeConverter<String[], String> {

  @Override
  public String convertToDatabaseColumn(String[] attribute) {
    if (attribute == null || attribute.length == 0) {
      return null;
    }
    return String.join("@@@", attribute);
  }

  @Override
  public String[] convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }
    return dbData.split("@@@");
  }
}
