package io.sunshower.arcus.persist.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.sunshower.arcus.persist.hibernate.entities.Person;
import io.sunshower.arcus.persist.hibernate.entities.Person2;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.val;
import org.junit.jupiter.api.Test;

@ArcusPersistenceTest
public class IdentifierPersistenceTest {

  @PersistenceContext private EntityManager entityManager;

  @Test
  void ensureEntityManagerIsConfigured() {
    assertNotNull(entityManager);
  }

  @Test
  @Transactional
  void ensurePersonCanBeSaved() {
    val person = new Person();
    person.setFirstname("Josiah");
    person.setLastName("Haswell");
    assertNull(person.getId());

    entityManager.persist(person);
    assertNotNull(person.getId());
    entityManager.flush();
    val results = entityManager.createQuery("select p from Person p", Person.class).getResultList();
    assertEquals(1, results.size());
  }

  @Test
  @Transactional
  void ensureManyWritesWorkWithUUIds() {
    long l1 = System.currentTimeMillis();

    for (int i = 0; i < 10000; i++) {
      val person = new Person2();
      person.setFirstname("Josiah" + i);
      person.setLastName("Haswell" + i);
      entityManager.persist(person);
      if (i % 1000 == 0) {
        entityManager.flush();
      }
    }
    entityManager.flush();
    long l2 = System.currentTimeMillis();

    System.out.format("UUIDs Persisted in %s\n", (l2 - l1));
  }

  @Test
  @Transactional
  void ensureManyWritesWorkWithFlakeIds() {
    long l1 = System.currentTimeMillis();

    for (int i = 0; i < 10000; i++) {
      val person = new Person();
      person.setFirstname("Josiah" + i);
      person.setLastName("Haswell" + i);
      entityManager.persist(person);
      if (i % 1000 == 0) {
        entityManager.flush();
      }
    }
    entityManager.flush();
    long l2 = System.currentTimeMillis();

    System.out.format("Flake Persisted in %s\n", (l2 - l1));
  }
}
