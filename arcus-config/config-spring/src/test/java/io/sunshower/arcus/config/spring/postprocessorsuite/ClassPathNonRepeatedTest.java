package io.sunshower.arcus.config.spring.postprocessorsuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sunshower.arcus.config.Configure;
import io.sunshower.arcus.config.Location;
import io.sunshower.arcus.config.spring.ConfigurationTestConfiguration;
import io.sunshower.arcus.config.spring.postprocessorsuite.ClassPathNonRepeatedTest.TestConfiguration;
import io.sunshower.arcus.lang.test.EnvironmentManager;
import io.sunshower.arcus.lang.test.Tests;
import io.sunshower.arcus.lang.test.Tests.Directories;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, ConfigurationTestConfiguration.class})
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

  @Test
  void ensureRetrievingConfigurationFromEnvironmentVariableWorks() {
    EnvironmentManager.props()
        .key("configuration.sample-configuration")
        .value(
            Tests.locateDirectoryPath(
                Directories.TestResources, "properties/sample-configuration.yaml")
                .toString())
        .with(
            e -> {
              val cfg =
                  new AnnotationConfigApplicationContext(
                      ClassPathNonRepeatedTest
                          .TestConfiguration.class,
                      ConfigurationTestConfiguration.class);
              try {
                val scfg = cfg.getBean(ClassPathNonRepeatedTest.SampleConfiguration.class);
                assertEquals("properties!", scfg.name);
              } finally {
                cfg.close();
              }
            });
  }


  @Test
  void ensureLoadingSampleConfigurationWorks() {
    try (val cfg = new AnnotationConfigApplicationContext(TestConfiguration2.class,
        ConfigurationTestConfiguration.class)) {
      val actualConfiguration = cfg.getBean(SampleConfiguration.class);
      assertEquals("properties!", actualConfiguration.name);
    }
  }


  @ContextConfiguration
  @Configure(value = SampleConfiguration.class, from = @Location("classpath:properties/sample-configuration.yaml"))
  static class TestConfiguration2 {

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
