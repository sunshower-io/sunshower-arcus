package io.sunshower.persist;

import io.sunshower.jpa.flyway.FlywayConfiguration;
import io.sunshower.persist.core.DataSourceConfiguration;
import io.sunshower.persist.hibernate.HibernateConfiguration;
import io.sunshower.persist.hibernate.TestConfig;
import io.sunshower.test.common.SerializationAware;
import io.sunshower.test.common.SerializationTestCase;
import io.sunshower.test.common.TestConfigurationConfiguration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
  classes = {
    TestConfig.class,
    FlywayConfiguration.class,
    DataSourceConfiguration.class,
    HibernateConfiguration.class,
    TestConfigurationConfiguration.class
  }
)
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public abstract class HibernateTestCase extends SerializationTestCase {

  final Logger logger = Logger.getLogger(getClass().getName());

  private Set<Object> toRemove;
  private Set<Object> toSave;

  @PersistenceContext private EntityManager entityManager;

  protected HibernateTestCase(SerializationAware.Format format, Class<?>[] bound) {
    super(format, bound);
    toSave = new HashSet<>();
    toRemove = new HashSet<>();
  }

  protected HibernateTestCase() {
    this(SerializationAware.Format.JSON, new Class[] {});
  }

  protected <T> T save(T value) {
    entityManager.persist(value);
    toRemove.add(value);
    return value;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void clear() {
    for (Object o : toRemove) {
      entityManager.remove(o);
    }
    toRemove.clear();
    toSave.clear();
    entityManager.flush();
  }
}
