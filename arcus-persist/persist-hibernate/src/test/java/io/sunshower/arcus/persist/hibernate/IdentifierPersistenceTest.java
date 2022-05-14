package io.sunshower.arcus.persist.hibernate;

import io.sunshower.persistence.annotations.Persistence;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ArcusPersistenceTest
public class IdentifierPersistenceTest {


  @PersistenceContext private EntityManager entityManager;


  @Test
  void ensureEntityManagerIsConfigured() {

  }
}
