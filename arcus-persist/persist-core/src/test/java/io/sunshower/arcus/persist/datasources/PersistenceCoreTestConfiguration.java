package io.sunshower.arcus.persist.datasources;

import io.sunshower.arcus.config.Configure;
import io.sunshower.arcus.config.Location;
import io.sunshower.arcus.config.spring.ArcusConfigurationBeanFactoryPostProcessor;
import io.sunshower.arcus.persist.datasources.PersistenceCoreTestConfiguration.PersistenceStubConfiguration;
import javax.xml.bind.annotation.XmlElement;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
@Configure(
    value = PersistenceStubConfiguration.class,
    from = @Location("classpath:scenarios/multiple-databases/configuration.yaml"))
public class PersistenceCoreTestConfiguration {

  @Bean
  public static BeanFactoryPostProcessor configurationAnnotationPostProcessor() {
    return new ArcusConfigurationBeanFactoryPostProcessor();
  }

  public static class PersistenceStubConfiguration {

    @XmlElement(name = "data-sources")
    DataSourcesConfiguration configuration;
  }
}
