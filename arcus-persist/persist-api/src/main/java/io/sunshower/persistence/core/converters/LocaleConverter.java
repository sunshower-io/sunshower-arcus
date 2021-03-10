package io.sunshower.persistence.core.converters;

import java.util.Locale;
import javax.persistence.AttributeConverter;

public class LocaleConverter implements AttributeConverter<Locale, String> {

  @Override
  public String convertToDatabaseColumn(Locale locale) {
    if (locale != null) {
      return locale.toLanguageTag();
    }
    return null;
  }

  @Override
  public Locale convertToEntityAttribute(String s) {
    if (!(s == null || s.isEmpty())) {
      return Locale.forLanguageTag(s);
    }
    return null;
  }
}
