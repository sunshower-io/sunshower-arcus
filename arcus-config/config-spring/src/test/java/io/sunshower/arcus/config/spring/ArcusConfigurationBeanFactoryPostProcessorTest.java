package io.sunshower.arcus.config.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sunshower.arcus.config.Configure;
import io.sunshower.arcus.config.spring.ArcusConfigurationBeanFactoryPostProcessorTest.TestConfiguration;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    TestConfiguration.class,
    ConfigurationTestConfiguration.class
})
class ArcusConfigurationBeanFactoryPostProcessorTest {

  @Inject
  private SampleConfiguration configuration;

  @Test
  void ensureClasspathConfigurationIsInjected() {
    assertNotNull(configuration);
  }

  @Test
  void ensureSampleConfigurationIsPopulated() {
    assertEquals(configuration.name, "hello");
  }

  @ContextConfiguration
  @Configure(SampleConfiguration.class)
  @Configure(SampleConfiguration2.class)
  static class TestConfiguration {

  }

  static class SampleConfiguration2 {

    @Getter
    @Setter
    private String value;
  }

  static class SampleConfiguration {

    @Getter
    @Setter
    private String name;
  }
}
