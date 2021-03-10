package io.sunshower.persistence.core;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import test.entities.hierarchy.Person;

@Transactional
public class TestService2 {
  @PersistenceContext private EntityManager entityManager;

  public void saveSecond() {

    final Person person = new Person();
    entityManager.persist(person);
    entityManager.flush();
  }
}
