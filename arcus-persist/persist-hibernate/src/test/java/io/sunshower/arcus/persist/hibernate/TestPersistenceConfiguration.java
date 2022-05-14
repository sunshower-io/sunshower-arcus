package io.sunshower.arcus.persist.hibernate;

import io.sunshower.persistence.config.DataSourceConfiguration;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.function.Predicate;
import javax.sql.DataSource;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TestPersistenceConfiguration {

  @Bean
  public DataSourceConfiguration dataSourceConfiguration() throws IOException {
    val cfg = DataSourceConfiguration.load(ClassLoader.getSystemResource("db-config.json"));
    if (!cfg.isEmpty()) {
      return cfg.get(0);
    }
    throw new NoSuchElementException("No data source configuration found");
  }

  @Bean
  public DataSource dataSource(DataSourceConfiguration configuration) {
    return createDataSource(configuration);
  }

  @Bean
  public LocalSessionFactoryBean sessionFactoryBean(DataSource dataSource,
      DataSourceConfiguration configuration) {
    val result = new LocalSessionFactoryBean();
    result.setDataSource(dataSource);
    result.setPackagesToScan(configuration.getScannedPackages());

    if (configuration.getAdditionalProperties() != null) {
      result.setHibernateProperties(
          fromMap(configuration.getAdditionalProperties(), k -> k.getKey().contains("hibernate")));
    }
    return result;
  }


  @Bean
  public PlatformTransactionManager transactionManager(LocalSessionFactoryBean factoryBean) {
    val transactionManager = new HibernateTransactionManager();
    transactionManager.setSessionFactory(factoryBean.getObject());
    return transactionManager;
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
      DataSourceConfiguration configuration) {
    val factorybean = new LocalContainerEntityManagerFactoryBean();
    factorybean.setDataSource(dataSource);
    factorybean.setPackagesToScan(configuration.getScannedPackages());
    val adapter = new HibernateJpaVendorAdapter();
    factorybean.setJpaVendorAdapter(adapter);
    return factorybean;
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

      ds.setConnectionProperties(
          fromMap(configuration.getAdditionalProperties(), p -> !p.getKey().contains("hibernate")));
    }
    ds.setUrl(configuration.getConnectionString());
    return ds;
  }

  Properties fromMap(Map<String, String> properties,
      Predicate<Map.Entry<String, String>> predicate) {
    val result = new Properties();
    for (val entry : properties.entrySet()) {
      if (predicate.test(entry)) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;

  }


}
