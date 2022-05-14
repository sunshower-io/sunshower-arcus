package io.sunshower.arcus.persist.hibernate;

import io.sunshower.persistence.config.DataSourceConfiguration;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class TestPersistenceConfiguration {

  @Bean
  public DataSource dataSource() throws IOException {
    val cfg = DataSourceConfiguration.load(ClassLoader.getSystemResource("db-config.json"));
    if (!cfg.isEmpty()) {
      return createDataSource(cfg.get(0));
    }
    throw new NoSuchElementException("No data source configuration found");
  }

  private DataSource createDataSource(DataSourceConfiguration configuration) {
    val ds = new DriverManagerDataSource();
    ds.setDriverClassName(configuration.getDriverClassName());
    if (configuration.getUsername() != null) {
      ds.setUsername(configuration.getUsername());
    }
    if (configuration.getPassword() != null) {
      ds.setPassword(configuration.getPassword());
    }

    if (configuration.getAdditionalProperties() != null) {
      val props = configuration.getAdditionalProperties();
      val nprops = new Properties();
      for (val prop : props.entrySet()) {
        nprops.setProperty(prop.getKey(), prop.getValue());
      }
      ds.setConnectionProperties(nprops);
    }
    ds.setUrl(configuration.getConnectionString());
    return ds;
  }


  @Bean
  public DataSourceConfiguration dataSourceConfiguration() {
    return null;

  }

//  protected Properties readProperties() {
//    val properties = new Properties();
//    val classloader = Thread.currentThread().getContextClassLoader();
//
//  }
}
