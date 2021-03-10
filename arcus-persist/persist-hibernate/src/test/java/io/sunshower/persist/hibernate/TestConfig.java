package io.sunshower.persist.hibernate;

import io.sunshower.persist.DefaultIndexedEntityService;
import io.sunshower.persist.IndexedEntityService;
import io.sunshower.persistence.Dialect;
import io.sunshower.persistence.annotations.CacheMode;
import io.sunshower.persistence.core.TestService;
import io.sunshower.persistence.core.TestService2;
import io.sunshower.test.common.TestConfigurationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@Import(TestConfigurationConfiguration.class)
@EnableAspectJAutoProxy(proxyTargetClass = true)
@CacheMode(CacheMode.Mode.Local)
public class TestConfig {

  @Bean
  public TestService testService() {
    return new TestService();
  }

  @Bean
  public TestService2 testService2() {
    return new TestService2();
  }

  @Bean
  public Dialect dialect() {
    return Dialect.Postgres;
  }

  @Bean
  public Pcfg pcfg() {
    return new Pcfg();
  }

  @Bean
  public IndexedEntityService indexedEntityService() {
    return new DefaultIndexedEntityService();
  }
}
