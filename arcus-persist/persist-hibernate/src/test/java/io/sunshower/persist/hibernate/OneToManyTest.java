package io.sunshower.persist.hibernate;

import io.sunshower.persist.HibernateTestCase;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import test.entities.one2many.Owned;
import test.entities.one2many.Owner;

@ContextConfiguration(classes = TestConfig.class)
public class OneToManyTest extends HibernateTestCase {

  @PersistenceContext private EntityManager entityManager;

  @Test
  public void ensureSavingOwnerWithChildrenWorks() {
    Owner owner = new Owner();
    owner.addOwned(new Owned());
    entityManager.persist(owner);
    entityManager.flush();
  }

  @Test
  public void ensureSavingIndividualOwnerWorks() {
    Owner owner = new Owner();
    entityManager.persist(owner);
    entityManager.flush();
  }
}
