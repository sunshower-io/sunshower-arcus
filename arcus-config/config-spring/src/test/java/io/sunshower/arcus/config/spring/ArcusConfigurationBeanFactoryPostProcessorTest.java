package io.sunshower.arcus.config.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages("io.sunshower.arcus.config.spring.postprocessorsuite")
@SuiteDisplayName("Arcus Bean Factory Post Processor Test Suite")
class ArcusConfigurationBeanFactoryPostProcessorTest {

  @Test
  void ensureSnakeCaseIsCorrect() {
    assertEquals(ArcusConfigurationBeanFactoryPostProcessor.snakeCase("HelloWorld"), "hello-world");
  }

  @Test
  void ensureEnvironmentCaseIsCorrect() {
    assertEquals(ArcusConfigurationBeanFactoryPostProcessor.toEnvironmentVariable("HelloWorld"),
        "ARCUS_HELLO_WORLD");
  }
}
