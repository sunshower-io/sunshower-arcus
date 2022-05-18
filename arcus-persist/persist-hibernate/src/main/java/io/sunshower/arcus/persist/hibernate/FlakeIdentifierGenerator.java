package io.sunshower.arcus.persist.hibernate;

import io.sunshower.persistence.id.Identifier;
import io.sunshower.persistence.id.Identifiers;
import io.sunshower.persistence.id.Sequence;
import lombok.NonNull;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public final class FlakeIdentifierGenerator implements IdentifierGenerator {

  final Sequence<Identifier> sequence;

  public FlakeIdentifierGenerator(@NonNull Sequence<Identifier> sequence) {
    this.sequence = sequence;
  }

  public FlakeIdentifierGenerator() {
    this(Identifiers.newSequence(true));
  }

  @Override
  public Object generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    return sequence.next();
  }
}
