package io.sunshower.crypt.vault;

import io.sunshower.arcus.condensation.Converter;
import io.sunshower.persistence.id.Identifier;

public class IdentifierConverter implements Converter<Identifier, String> {

  @Override
  public Identifier read(String s) {
    if (s == null) {
      return null;
    }
    return Identifier.valueOf(s);
  }

  @Override
  public String write(Identifier identifier) {
    if (identifier == null) {
      return null;
    }
    return identifier.toString();
  }
}
