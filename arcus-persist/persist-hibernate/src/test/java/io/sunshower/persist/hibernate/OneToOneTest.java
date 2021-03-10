package io.sunshower.persist.hibernate;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import io.sunshower.common.Identifier;
import io.sunshower.persist.HibernateTestCase;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import test.entities.one2one.Ownee;
import test.entities.one2one.Owner;

@Rollback
@ContextConfiguration(classes = HibernateConfiguration.class)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class OneToOneTest extends HibernateTestCase {

  @PersistenceContext private EntityManager entityManager;

  static Identifier id;

  public void validateDb() {
    List objects = entityManager.createQuery("select o from Owner o").getResultList();
    System.out.println(objects);
    //    assertThat(objects.isEmpty(), is(true));
  }

  @Test
  @Rollback
  public void a_saveRevision1() {
    validateDb();
    final Owner owner = new Owner();
    owner.setName("frapper");
    entityManager.persist(owner);
    id = owner.getId();
    entityManager.flush();

    assertThat(entityManager.find(Owner.class, id), is(not(nullValue())));
  }

  @Test
  @Rollback
  public void b_saveRevision2() {
    validateDb();
    Owner owner = new Owner();
    owner.setName("frapper");
    entityManager.persist(owner);
    entityManager.flush();
    owner =
        entityManager
            .createQuery("select e from Owner e where e.id = :id", Owner.class)
            .setParameter("id", owner.getId())
            .getSingleResult();
    Ownee ownee = new Ownee();
    ownee.setName("froadfasdf");
    owner.setOwnee(ownee);
    entityManager.merge(owner);
  }

  @Test
  @Rollback
  public void ensureSavingSingleOwnerWorks() {
    validateDb();
    final Owner owner = new Owner();
    entityManager.persist(owner);
    entityManager.flush();
  }

  @Test
  @Rollback
  public void ensureSavingBidirectionalOwnerWorksWithCascade() {
    validateDb();
    Owner owner = new Owner();
    Ownee ownee = new Ownee();
    ownee.setName("coobleans");
    owner.setOwnee(ownee);
    entityManager.persist(owner);
    entityManager.flush();
  }
}
