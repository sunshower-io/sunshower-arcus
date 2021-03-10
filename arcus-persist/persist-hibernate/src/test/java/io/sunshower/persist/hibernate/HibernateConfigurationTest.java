package io.sunshower.persist.hibernate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import io.sunshower.jpa.flyway.FlywayConfiguration;
import io.sunshower.persist.core.DataSourceConfiguration;
import io.sunshower.persistence.PersistenceUnit;
import io.sunshower.persistence.core.MachineAddress;
import io.sunshower.persistence.core.NetworkAddress;
import java.util.Arrays;
import java.util.HashSet;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import lombok.val;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import test.entities.SampleEntity;
import test.entities.TestEntity;

@ExtendWith(SpringExtension.class)
@Transactional
@ContextConfiguration(
  classes = {
    TestConfig.class,
    FlywayConfiguration.class,
    DataSourceConfiguration.class,
    PersistenceAnnotationBeanPostProcessor.class,
    HibernateConfiguration.class
  }
)
@TestExecutionListeners({
  DependencyInjectionTestExecutionListener.class,
  TransactionalTestExecutionListener.class,
  DirtiesContextTestExecutionListener.class,
})
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class HibernateConfigurationTest {

  @Inject private PersistenceUnit configuration;

  @PersistenceContext private EntityManager entityManager;

  @Inject private TestConfig contextConfiguration;

  @Test
  void ensureMapperWorks() {
    val e = new SampleEntity();
    e.setValues(new String[] {"Hello", "World"});
    entityManager.persist(e);
    entityManager.flush();
    assertThat(entityManager.find(SampleEntity.class, e.getId()).getValues()[0], is("Hello"));
  }

  @Test
  public void ensureEntityWithComplexRelationshipCanBeSaved() {
    TestEntity e = new TestEntity();
    MachineAddress address = new MachineAddress("asdfasdfasdf".toCharArray());
    e.setMac(address);
    NetworkAddress address1 = new NetworkAddress("asdfasdfasdf".toCharArray());
    e.setInet(address1);
    e.addChild(new TestEntity());
    entityManager.persist(e);
    entityManager.flush();
    assertThat(entityManager.find(TestEntity.class, e.getId()).getChildren().size(), is(1));
    assertThat(
        entityManager.find(TestEntity.class, e.getId()).getChildren().iterator().next().getParent(),
        is(e));
    assertThat(entityManager.find(TestEntity.class, e.getId()).getMac(), is(address));
    assertThat(entityManager.find(TestEntity.class, e.getId()).getInet(), is(address1));
  }

  @Test
  public void ensureConfigurationIsInjected() {
    assertThat(configuration, is(not(nullValue())));
  }

  @Test
  public void ensureConfigurationHasCorrectScannedPacakges() {
    HashSet<String> strings =
        new HashSet<>(
            Arrays.asList(
                "test.entities",
                "test.entities.one2one",
                "test.entities.one2many",
                "test.entities.many2many"));
    HashSet<String> actual = new HashSet<>(Arrays.asList(configuration.getScannedPackages()));
    assertThat(strings, is(actual));

    assertThat(strings.containsAll(actual), is(true));
    assertThat(actual.containsAll(strings), is(true));
  }

  @Test
  public void ensureEntityCanBeSavedAndFlushed() {
    final TestEntity entity = new TestEntity();
    entityManager.persist(entity);
    entityManager.flush();
  }

  @Test
  public void ensureContextConfigurationIsInjected() {
    assertThat(contextConfiguration, is(not(nullValue())));
  }

  public static class Cfg {}
}
