package io.sunshower.persist.hibernate;

import io.sunshower.jpa.configuration.JpaProviderProperties;
import java.util.Properties;

public class HibernateProviderConfigurationSource implements JpaProviderProperties {

  private final DataDefinitionLanguage ddl;
  private final SearchConfiguration search;
  private final HibernateCacheConfiguration cache;
  private final HibernateDialectProperties provider;

  public HibernateProviderConfigurationSource(
      final DataDefinitionLanguage ddl,
      final SearchConfiguration search,
      final HibernateDialectProperties provider,
      final HibernateCacheConfiguration cache) {
    this.ddl = ddl;
    this.cache = cache;
    this.search = search;
    this.provider = provider;
  }

  public DataDefinitionLanguage ddl() {
    return ddl;
  }

  public HibernateCacheConfiguration cache() {
    return cache;
  }

  public HibernateDialectProperties getProvider() {
    return provider;
  }

  public SearchConfiguration getSearch() {
    return search;
  }

  public HibernateCacheConfiguration getCache() {
    return cache;
  }

  public Properties toNative() {
    final Properties properties = new Properties();
    configureSearch(properties);

    configureDialect(properties);
    configureDiagnostics(properties);

    return properties;
  }

  private void configureDialect(Properties properties) {
    properties.put("jpa.dialect", provider.dialect());
  }

  private void configureDiagnostics(Properties properties) {
    properties.setProperty("hibernate.show_sql", Boolean.toString(ddl.showSql()));
    properties.setProperty("hibernate.format_sql", Boolean.toString(ddl.formatSql()));
    if (ddl.generate()) {
      properties.setProperty("hibernate.hbm2ddl.auto", ddl.strategy());
    }
  }

  private void configureSearch(Properties properties) {
    if (search != null) {
      properties.setProperty(search.type(), search.value());
    }
  }
}
