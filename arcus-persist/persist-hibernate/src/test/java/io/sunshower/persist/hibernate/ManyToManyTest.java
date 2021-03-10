package io.sunshower.persist.hibernate;

import io.sunshower.persist.HibernateTestCase;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import test.entities.many2many.BlogEntry;
import test.entities.many2many.Tag;

@ContextConfiguration(classes = TestConfig.class)
public class ManyToManyTest extends HibernateTestCase {

  @PersistenceContext private EntityManager entityManager;

  @Test
  public void ensureSavingManyToManyWorks() {
    BlogEntry entry = new BlogEntry();
    entry.addTag(new Tag("Coolbeans"));
    entry.addTag(new Tag("Coolbeans"));
    entityManager.persist(entry);
    entityManager.flush();
  }
}
