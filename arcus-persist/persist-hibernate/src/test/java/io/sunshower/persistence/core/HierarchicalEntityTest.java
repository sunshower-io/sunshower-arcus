package io.sunshower.persistence.core;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import io.sunshower.common.Identifier;
import io.sunshower.persist.HibernateTestCase;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.annotation.Transactional;
import test.entities.hierarchy.Person;

@Transactional
public class HierarchicalEntityTest extends HibernateTestCase {

  @Inject private TestService testService;
  @PersistenceContext private EntityManager entityManager;

  @Test
  public void ensureSavingPersonWorks() {
    final Person person = new Person();
    person.addChild(new Person());
    entityManager.persist(person);
    entityManager.flush();
  }

  @Test
  public void ensuretransactionPropagationWorks() {

    final Person person = new Person();
    person.addChild(new Person());
    testService.saveEntity(person);
  }

  @Test
  void ensureIdentifierWorksInQuery() {
    List<Person> id =
        entityManager
            .createQuery("select e from Person e where e.id = :id", Person.class)
            .setParameter("id", Identifier.random())
            .getResultList();
    assertThat(id.isEmpty(), is(true));
  }

  @Test
  void ensureFindingPersonByIdInQueryWorks() {
    final Person person = new Person();
    person.addChild(new Person());
    entityManager.persist(person);
    entityManager.flush();

    List<Person> id =
        entityManager
            .createQuery("select e from Person e where e.id = :id", Person.class)
            .setParameter("id", person.getId())
            .getResultList();
    assertThat(id.contains(person), is(true));
  }
}
