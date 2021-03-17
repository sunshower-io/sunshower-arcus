package io.sunshower.arcus.config.spring.postprocessorsuite;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.sunshower.arcus.config.Configure;
import io.sunshower.arcus.config.spring.ConfigurationTestConfiguration;
import io.sunshower.arcus.config.spring.postprocessorsuite.ClassPathRepeatedTest.TestConfiguration;
import io.sunshower.arcus.config.test.ArcusEnvironmentTestExecutionListener;
import io.sunshower.arcus.config.test.EnvironmentVariable;
import io.sunshower.arcus.config.test.WithEnvironment;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfiguration.class, ConfigurationTestConfiguration.class})
@TestExecutionListeners(
    listeners = ArcusEnvironmentTestExecutionListener.class,
    mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
class ClassPathRepeatedTest {

  @Inject private SampleConfiguration configuration;

  @Test
  void ensureClasspathConfigurationIsInjected() {
    assertNotNull(configuration);
  }

  @Test
  void ensureSampleConfigurationIsPopulated() {
    assertEquals(configuration.name, "hello");
  }

  @Test
  @WithEnvironment(
      variables = {
        @EnvironmentVariable(
            key = "ARCUS_SAMPLE_CONFIGURATION",
            value = "${Tests.TestResources}/environment/sample-configuration.yaml")
      })
  void ensureLoadingSampleConfigurationWorks() {}

  @ContextConfiguration
  @Configure(SampleConfiguration.class)
  @Configure(SampleConfiguration2.class)
  static class TestConfiguration {}

  static class SampleConfiguration2 {

    @Getter @Setter private String value;
  }

  static class SampleConfiguration {

    @Getter @Setter private String name;
  }
}
