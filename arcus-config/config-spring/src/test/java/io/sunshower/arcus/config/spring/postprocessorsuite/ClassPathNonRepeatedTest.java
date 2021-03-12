package io.sunshower.arcus.config.spring.postprocessorsuite;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sunshower.arcus.config.Configure;
import io.sunshower.arcus.config.spring.ConfigurationTestConfiguration;
import io.sunshower.arcus.config.spring.postprocessorsuite.ClassPathNonRepeatedTest.TestConfiguration;
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
class ClassPathNonRepeatedTest {

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
  static class TestConfiguration {

  }


  static class SampleConfiguration {

    @Getter
    @Setter
    private String name;
  }
}

