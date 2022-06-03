package io.sunshower.arcus.persist.flyway;

import io.sunshower.arcus.persist.hibernate.TestPersistenceConfiguration;
import io.sunshower.persistence.MigrationManager;
import io.sunshower.persistence.config.DataSourceConfiguration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.function.Predicate;
import javax.sql.DataSource;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
public class FlywayTestConfiguration extends TestPersistenceConfiguration {

  @Bean
  public MigrationManager migrationManager(
      DataSource dataSource, DataSourceConfiguration configuration) {
    val result = new ArcusFlywayMigrationManager(configuration);
    result.apply(dataSource);
    return result;
  }

  @Bean
  @Primary
  @DependsOn("migrationManager")
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource, DataSourceConfiguration configuration) {
    val factorybean = new LocalContainerEntityManagerFactoryBean();
    if (configuration.getAdditionalProperties() != null) {
      factorybean.setJpaProperties(
          fromMap(configuration.getAdditionalProperties(), p -> p.getKey().contains("hibernate")));
    }

    factorybean.setDataSource(dataSource);
    factorybean.setPackagesToScan(configuration.getScannedPackages());
    val adapter = new HibernateJpaVendorAdapter();
    adapter.setGenerateDdl(true);
    adapter.setPrepareConnection(true);
    adapter.setShowSql(false);
    factorybean.setJpaVendorAdapter(adapter);
    return factorybean;
  }

  Properties fromMap(Map<String, String> properties, Predicate<Entry<String, String>> predicate) {
    val result = new Properties();
    for (val entry : properties.entrySet()) {
      if (predicate.test(entry)) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }
}
