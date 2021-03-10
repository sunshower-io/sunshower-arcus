package io.sunshower.persist.hibernate;

import java.util.Properties;
import org.cfg4j.provider.ConfigurationProvider;

public class Configurations {

  public static Properties toNative(
      ConfigurationProvider provider, HibernateProviderConfigurationSource source) {
    final Properties properties = new Properties();
    final HibernateDialectProperties dialect = resolve(provider, source);
    configureSearch(dialect, properties);
    configureDialect(dialect, properties);
    configureDiagnostics(dialect, properties);

    return properties;
  }

  private static HibernateDialectProperties resolve(
      ConfigurationProvider provider, HibernateProviderConfigurationSource source) {
    return null;
  }

  private static void configureDialect(HibernateDialectProperties dialect, Properties properties) {
    //        properties.put("jpa.dialect", provider.getDialect());
  }

  private static void configureDiagnostics(
      HibernateDialectProperties dialect, Properties properties) {
    //        DataDefinitionLanguage ddl = provider.getDdl();
    //        properties.setProperty("hibernate.show_sql", Boolean.toString(ddl.isShowSql()));
    //        properties.setProperty("hibernate.format_sql", Boolean.toString(ddl.isFormatSql()));
    //        if(ddl.isGenerate()) {
    //            properties.setProperty("hibernate.hbm2ddl.auto", ddl.getStrategy());
    //        }
  }

  private static void configureSearch(HibernateDialectProperties dialect, Properties properties) {
    //        SearchConfiguration search = provider.getSearch();
    //        if(search != null) {
    //            properties.setProperty(search.getType(), search.getValue());
    //        }
  }

  public static HibernateDialectProperties getProvider(ConfigurationProvider cfgProvider) {
    return null;
  }
}
