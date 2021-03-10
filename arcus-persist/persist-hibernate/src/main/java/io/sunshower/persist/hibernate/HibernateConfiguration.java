package io.sunshower.persist.hibernate;

import io.sunshower.persist.validation.ModelValidator;
import io.sunshower.persistence.PersistenceUnit;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;
import org.cfg4j.provider.ConfigurationProvider;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class HibernateConfiguration extends HibernateConfigurer {

  @Bean
  @Override
  public HibernateProviderConfigurationSource hibernateProviderConfigurationSource(
      DataDefinitionLanguage ddl,
      SearchConfiguration searchConfiguration,
      HibernateDialectProperties props,
      HibernateCacheConfiguration cacheConfiguration) {
    return super.hibernateProviderConfigurationSource(
        ddl, searchConfiguration, props, cacheConfiguration);
  }

  @Bean
  @Override
  public DataDefinitionLanguage dataDefinitionLanguage(ConfigurationProvider source) {
    return super.dataDefinitionLanguage(source);
  }

  @Bean
  @Override
  public HibernateCacheConfiguration hibernateCacheConfiguration(ConfigurationProvider source) {
    return super.hibernateCacheConfiguration(source);
  }

  @Bean
  @Override
  public SearchConfiguration searchConfiguration(ConfigurationProvider source) {
    return super.searchConfiguration(source);
  }

  @Bean
  @Override
  public HibernateDialectProperties hibernateDialectProperties(ConfigurationProvider source) {
    return super.hibernateDialectProperties(source);
  }

  @Bean
  @Override
  public UserTransaction userTransaction() {
    return super.userTransaction();
  }

  @Bean
  @Override
  public TransactionManager jtaTransactionManager() {
    return super.jtaTransactionManager();
  }

  @Bean
  @Override
  public TransactionSynchronizationRegistry transactionSynchronizationRegistry() {
    return super.transactionSynchronizationRegistry();
  }

  @Bean
  @Override
  public PlatformTransactionManager transactionManager(
      EntityManagerFactory entityManagerFactory,
      DataSource dataSource,
      UserTransaction userTransaction,
      TransactionManager txManager,
      TransactionSynchronizationRegistry registry) {
    return super.transactionManager(
        entityManagerFactory, dataSource, userTransaction, txManager, registry);
  }

  @Bean
  @Override
  public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
    return super.persistenceAnnotationBeanPostProcessor();
  }

  @Bean
  @Override
  public ModelValidator modelValidator() {
    return super.modelValidator();
  }

  @Bean
  @Override
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource,
      HibernateProviderConfigurationSource source,
      PersistenceUnit persistenceConfiguration,
      ConfigurationProvider provider) {
    return super.entityManagerFactory(dataSource, source, persistenceConfiguration, provider);
  }

  @Override
  protected void configureCache(
      Properties jpaProperties,
      ConfigurationProvider cfgProvider,
      HibernateCacheConfiguration cache,
      HibernateProviderConfigurationSource source) {
    super.configureCache(jpaProperties, cfgProvider, cache, source);
  }

  @Bean
  @Override
  public FullTextEntityManager entityManager(EntityManagerFactory entityManagerFactory) {
    return super.entityManager(entityManagerFactory);
  }
}
