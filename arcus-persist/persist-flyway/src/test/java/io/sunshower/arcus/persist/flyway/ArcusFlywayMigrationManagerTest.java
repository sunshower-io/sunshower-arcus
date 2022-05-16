package io.sunshower.arcus.persist.flyway;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.sunshower.arcus.persist.flyway.entities.Person;
import io.sunshower.arcus.persist.hibernate.ArcusPersistenceTest;
import io.sunshower.arcus.persist.hibernate.TestPersistenceConfiguration;
import io.sunshower.persistence.config.DataSourceConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ArcusPersistenceTest
@ContextConfiguration(classes = {FlywayTestConfiguration.class, TestPersistenceConfiguration.class})
class ArcusFlywayMigrationManagerTest {

  @PersistenceContext EntityManager entityManager;

  @Test
  void ensureManagerIsInjected() {
    assertNotNull(entityManager);
  }

  @Test
  void ensureDataSourceIsInjected(
      @Autowired DataSource dataSource, @Autowired DataSourceConfiguration configuration)
      throws SQLException, InterruptedException {
    dataSource.getConnection().prepareStatement("select count(*) from PEOPLE t").executeQuery();
  }

  @Test
  @Transactional
  void ensureSavingPersonWorks() {
    val person = new Person();
    assertNull(person.getId());
    entityManager.persist(person);
    entityManager.flush();
    assertNotNull(person.getId());
  }

  @Test
  @Transactional
  void ensureRetrievingPersonByIdWorks() {
    val person = new Person();
    entityManager.persist(person);
    entityManager.flush();
    assertEquals(person.getId(), entityManager.find(Person.class, person.getId()).getId());
  }
}
