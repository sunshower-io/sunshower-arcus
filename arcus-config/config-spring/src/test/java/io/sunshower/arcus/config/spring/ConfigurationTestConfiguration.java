package io.sunshower.arcus.config.spring;

import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class ConfigurationTestConfiguration {

  @Bean
  public static BeanFactoryPostProcessor arcusConfiguration() {
    return new ArcusConfigurationBeanFactoryPostProcessor();
  }
}
