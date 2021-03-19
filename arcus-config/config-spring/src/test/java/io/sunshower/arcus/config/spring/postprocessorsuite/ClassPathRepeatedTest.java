package io.sunshower.arcus.config.spring.postprocessorsuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sunshower.arcus.config.Configure;
import io.sunshower.arcus.config.spring.ConfigurationTestConfiguration;
import io.sunshower.arcus.config.spring.postprocessorsuite.ClassPathRepeatedTest.TestConfiguration;
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
class ClassPathRepeatedTest {

  @Inject
  private SampleConfiguration configuration;

  @Test
  void ensureConfigurationCanBeLoadedFromEnvironmentVariable() {
    EnvironmentManager.env().key("ARCUS_SAMPLE_CONFIGURATION").value(Tests.locateDirectoryPath(
        Directories.TestResources, "env/sample-configuration.yaml").toString()).with(e -> {
      val cfg = new AnnotationConfigApplicationContext(TestConfiguration.class,
          ConfigurationTestConfiguration.class);
      try {
        val scfg = cfg.getBean(SampleConfiguration.class);
        assertEquals("environment!", scfg.name);
      } finally {
        cfg.close();
      }
    });
  }

  @Test
  void ensureConfigurationCanBeLoadedFromSystemProperty() {
    EnvironmentManager.props().key("configuration.sample-configuration").value(Tests.locateDirectoryPath(
        Directories.TestResources, "properties/sample-configuration.yaml").toString()).with(e -> {
      val cfg = new AnnotationConfigApplicationContext(TestConfiguration.class,
          ConfigurationTestConfiguration.class);
      try {
        val scfg = cfg.getBean(SampleConfiguration.class);
        assertEquals("properties!", scfg.name);
      } finally {
        cfg.close();
      }
    });
  }

  @Test
  void ensureClasspathConfigurationIsInjected() {
    assertNotNull(configuration);
  }

  @Test
  void ensureSampleConfigurationIsPopulated() {
    assertEquals(configuration.name, "hello");
  }

  @Test
  void ensureLoadingSampleConfigurationWorks() {
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
