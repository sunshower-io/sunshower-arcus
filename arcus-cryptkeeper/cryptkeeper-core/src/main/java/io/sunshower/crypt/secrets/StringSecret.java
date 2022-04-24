package io.sunshower.crypt.secrets;

import io.sunshower.crypt.core.Secret;
import io.sunshower.persistence.id.Identifier;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringSecret implements Secret {

  private Identifier id;
  private CharSequence name;
  private CharSequence material;
  private CharSequence description;

  public StringSecret() {}

  public StringSecret(CharSequence name, CharSequence description, CharSequence material) {
    this.id = Secret.IDENTIFIER_SEQUENCE.next();
    this.name = name;
    this.material = material;
    this.description = description;
  }

  @Override
  public Identifier getId() {
    return id;
  }
}
