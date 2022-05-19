package io.sunshower.arcus.persist.hibernate;

import io.sunshower.persistence.id.Identifier;
import io.sunshower.persistence.id.Identifiers;
import io.sunshower.persistence.id.Sequence;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.val;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class FlakeGenerationTypeStrategy implements IdentifierGenerator {

  private final Map<String, Sequence<Identifier>> sequences;

  public FlakeGenerationTypeStrategy() {
    sequences = new HashMap<>();
  }

  @Override
  public Serializable generate(SharedSessionContractImplementor session, Object object)
      throws HibernateException {
    synchronized (sequences) {
      val name = object.getClass().getName();
      return sequences.computeIfAbsent(name, e -> Identifiers.newSequence(true)).next();
    }
  }
}
