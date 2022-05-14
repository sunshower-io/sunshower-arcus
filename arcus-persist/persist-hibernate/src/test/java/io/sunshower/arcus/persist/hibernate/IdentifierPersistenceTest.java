package io.sunshower.arcus.persist.hibernate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.sunshower.arcus.persist.hibernate.entities.Person;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.val;
import org.junit.jupiter.api.Test;

@ArcusPersistenceTest
public class IdentifierPersistenceTest {


  @PersistenceContext
  private EntityManager entityManager;


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
}
