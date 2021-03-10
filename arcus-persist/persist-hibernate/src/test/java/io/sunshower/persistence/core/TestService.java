package io.sunshower.persistence.core;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;
import test.entities.hierarchy.Person;

@Transactional
public class TestService {

  @Inject private TestService2 testService;

  @PersistenceContext private EntityManager entityManager;

  public void saveEntity(Person p) {
    entityManager.persist(p);
    entityManager.flush();
    testService.saveSecond();
  }
}
