package io.sunshower.persist.hibernate;

import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionManagerImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.TransactionSynchronizationRegistryImple;
import com.arjuna.ats.internal.jta.transaction.arjunacore.UserTransactionImple;
import io.sunshower.persist.validation.ModelValidator;
import io.sunshower.persistence.PersistenceUnit;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.TransactionSynchronizationRegistry;
import javax.transaction.UserTransaction;
import org.cfg4j.provider.ConfigurationProvider;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

public class HibernateConfigurer {
  private static final String STANDARD_QUERY_CACHE_NAME =
      "org.hibernate.cache.internal.StandardQueryCache";
  private static final String UPDATE_TIMESTAMPS_CACHE_NAME =
      "org.hibernate.cache.spi.UpdateTimestampsCache";

  static final Logger log = Logger.getLogger(HibernateConfiguration.class.getName());

  protected HibernateProviderConfigurationSource hibernateProviderConfigurationSource(
      DataDefinitionLanguage ddl,
      SearchConfiguration searchConfiguration,
      HibernateDialectProperties props,
      HibernateCacheConfiguration cacheConfiguration) {
    return new HibernateProviderConfigurationSource(
        ddl, searchConfiguration, props, cacheConfiguration);
  }

  public DataDefinitionLanguage dataDefinitionLanguage(ConfigurationProvider source) {
    return source.bind("jpa.provider.ddl", DataDefinitionLanguage.class);
  }

  public HibernateCacheConfiguration hibernateCacheConfiguration(ConfigurationProvider source) {
    return source.bind("jpa.provider.cache", HibernateCacheConfiguration.class);
  }

  public SearchConfiguration searchConfiguration(ConfigurationProvider source) {
    return source.bind("jpa.provider.search", SearchConfiguration.class);
  }

  public HibernateDialectProperties hibernateDialectProperties(ConfigurationProvider source) {
    return source.bind("jpa.provider", HibernateDialectProperties.class);
  }

  public UserTransaction userTransaction() {
    return new UserTransactionImple();
  }

  public TransactionManager jtaTransactionManager() {
    return new TransactionManagerImple();
  }

  public TransactionSynchronizationRegistry transactionSynchronizationRegistry() {
    return new TransactionSynchronizationRegistryImple();
  }

  public PlatformTransactionManager transactionManager(
      EntityManagerFactory entityManagerFactory,
      DataSource dataSource,
      UserTransaction userTransaction,
      TransactionManager txManager,
      TransactionSynchronizationRegistry registry) {
    final JtaTransactionManager transactionManager = new JtaTransactionManager();
    transactionManager.setUserTransaction(userTransaction);
    transactionManager.setTransactionManager(txManager);
    transactionManager.setAllowCustomIsolationLevels(true);
    transactionManager.setDefaultTimeout((int) TimeUnit.MINUTES.toMillis(20));

    transactionManager.setTransactionSynchronizationRegistry(registry);
    return transactionManager;
  }

  public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor() {
    return new PersistenceAnnotationBeanPostProcessor();
  }

  public ModelValidator modelValidator() {
    return new ModelValidator();
  }

  public LocalContainerEntityManagerFactoryBean entityManagerFactory(
      DataSource dataSource,
      HibernateProviderConfigurationSource source,
      PersistenceUnit persistenceConfiguration,
      ConfigurationProvider provider) {
    LocalContainerEntityManagerFactoryBean entityManagerFactoryBean =
        new LocalContainerEntityManagerFactoryBean();
    entityManagerFactoryBean.setPersistenceUnitName("default-persistence-unit");
    entityManagerFactoryBean.setJtaDataSource(dataSource);
    final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setDatabase(Database.POSTGRESQL);
    vendorAdapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQL94Dialect");
    entityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);
    entityManagerFactoryBean.setPackagesToScan(persistenceConfiguration.getScannedPackages());

    Properties properties = Configurations.toNative(provider, source);
    configureCache(properties, provider, source.getCache(), source);
    entityManagerFactoryBean.setJpaProperties(properties);
    return entityManagerFactoryBean;
  }

  protected void configureCache(
      Properties jpaProperties,
      ConfigurationProvider cfgProvider,
      HibernateCacheConfiguration cache,
      HibernateProviderConfigurationSource source) {
    HibernateDialectProperties provider = Configurations.getProvider(cfgProvider);
    if (provider == null) {
      log.info("No L2 Cache configured");
    }
    jpaProperties.put("hibernate.connection.autocommit", "true");

    if (cache == null) {
      log.info("No L2 Cache configured");
    } else {
      if (!cache.enabled()) {
        log.info("L2 Cache is disabled");
      } else {
        String cacheProvider = cache.provider();
        log.info("L2 Cache is enabled");
        log.info("Cache provider: '" + cacheProvider + "'");
        jpaProperties.put("hibernate.cache.use_second_level_cache", "true");
      }
      if (cache.enableQueryCache()) {
        log.info("Query cache is enabled");
        jpaProperties.put("hibernate.cache.use_query_cache", "true");
      }
      jpaProperties.put("hibernate.cache.region.factory_class", cache.regionFactory());
      jpaProperties.put("org.apache.ignite.hibernate.grid_name", cache.fabricName());
      jpaProperties.put(
          "hibernate.cache.infinispan.cfg",
          "org/infinispan/hibernate/cache/commons/builder/infinispan-configs-local.xml");
    }

    jpaProperties.put(
        "hibernate.transaction.factory_class", "org.hibernate.transaction.JTATransactionFactory");

    jpaProperties.put(
        "hibernate.transaction.jta.platform",
        "org.hibernate.service.jta.platform.internal.JBossStandAloneJtaPlatform");
    jpaProperties.put(
        "hibernate.transaction.manager_lookup_class",
        "org.hibernate.transaction.JBossTransactionManagerLookup");

    //    jpaProperties.put("hibernate.connection.release_mode", "on_close");
    jpaProperties.put("hibernate.connection.acquisition_mode", "immediate");
  }

  public FullTextEntityManager entityManager(EntityManagerFactory entityManagerFactory) {
    return Search.getFullTextEntityManager(entityManagerFactory.createEntityManager());
  }
}
