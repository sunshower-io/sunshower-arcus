package io.sunshower.arcus.persist.flyway;

import io.sunshower.persistence.config.DataSourceConfiguration;
import jakarta.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayTestConfiguration implements ApplicationContextAware {

  @PostConstruct
  public void setUp() {}

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    val dataSource = applicationContext.getBean(DataSource.class);
    val configuration = applicationContext.getBean(DataSourceConfiguration.class);
    val manager = new ArcusFlywayMigrationManager(configuration);
    manager.apply(dataSource);
  }
}
