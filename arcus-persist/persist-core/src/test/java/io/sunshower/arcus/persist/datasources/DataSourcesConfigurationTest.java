package io.sunshower.arcus.persist.datasources;

import io.sunshower.arcus.persist.datasources.PersistenceCoreTestConfiguration.PersistenceStubConfiguration;
import javax.inject.Inject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = PersistenceCoreTestConfiguration.class)
class DataSourcesConfigurationTest {

  @Inject
  private PersistenceStubConfiguration configuration;

  @Test
  void test() {}
}
