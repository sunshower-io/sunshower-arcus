package io.sunshower.crypt.core;

import io.sunshower.persistence.id.Identifier;
import io.sunshower.persistence.id.Identifiers;
import io.sunshower.persistence.id.Sequence;
import java.io.Serializable;

public interface Secret extends Serializable {

  Sequence<Identifier> IDENTIFIER_SEQUENCE = Identifiers.newSequence(true);

  CharSequence getName();

  CharSequence getDescription();

  Identifier getId();
}
