package io.sunshower.crypt.vault;

import io.sunshower.arcus.condensation.Converter;
import io.sunshower.persistence.id.Identifier;

public class IdentifierConverter implements Converter<Identifier, String> {

  @Override
  public Identifier read(String s) {
    return Identifier.valueOf(s);
  }

  @Override
  public String write(Identifier identifier) {
    return identifier.toString();
  }
}
