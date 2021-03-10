package io.sunshower.persist.core;

import static io.sunshower.persist.core.DataSourceConfigurations.useLocation;

import io.sunshower.common.Identifier;
import io.sunshower.persistence.Dialect;
import io.sunshower.persistence.UnsupportedDatabaseException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.inject.Singleton;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.cfg4j.provider.ConfigurationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class DataSourceConfiguration {

  static final Logger log = LoggerFactory.getLogger(DataSourceConfiguration.class);

  @Bean
  @Singleton
  public Dialect databaseDialect(DataSource dataSource) throws SQLException {
    try (final Connection cnx = dataSource.getConnection()) {
      final String name = cnx.getMetaData().getDatabaseProductName().toLowerCase();
      switch (name) {
        case "h2":
          log.info("dialect is 'h2'");
          return Dialect.H2;
        case "postgresql":
          log.info("dialect is 'postgres'");
          return Dialect.Postgres;
      }
      throw new UnsupportedDatabaseException("Sunshower does not support the database: " + name);
    }
  }

  @Bean
  public DatabaseConfigurationSource databaseConfigurationSource(ConfigurationProvider provider) {
    return provider.bind("jdbc", DatabaseConfigurationSource.class);
  }

  @Bean
  public DataSource dataSource(DatabaseConfigurationSource cfg) throws NamingException {
    if (useLocation(cfg)) {
      log.info("Starting JDBC data-source...");

      final DriverManagerDataSource result = new DriverManagerDataSource();
      //      final DataSource result = new HikariDataSource(toNative(cfg));
      //      final HikariDataSource result = new HikariDataSource();
      result.setDriverClassName("com.arjuna.ats.jdbc.TransactionalDriver");
      final Properties properties = new Properties();
      properties.setProperty("user", "sa");
      properties.setProperty("password", "");
      properties.setProperty("DYNAMIC_CLASS", "io.sunshower.persist.core.JtaDynamicClass");
      //
      result.setConnectionProperties(properties);
      //      result.setMaximumPoolSize(1);

      result.setUrl(process(cfg.url()));
      //
      // "jdbc:arjuna:h2:mem:frapper;MODE=PostgreSQL;LOCK_MODE=0;MV_STORE=false;DB_CLOSE_DELAY=-1;"));
      //      result.setUrl(
      //          process(
      //
      // "jdbc:arjuna:h2:mem:frapper;MODE=PostgreSQL;LOCK_MODE=0;MV_STORE=false;DB_CLOSE_DELAY=-1;"));
      log.info("Successfully started data-source");
      return result;
    } else {
      log.info("Starting JNDI data-source...");
      final DataSource result = InitialContext.doLookup(cfg.jndiPath());
      log.info("Successfully started data-source");
      return result;
    }
  }

  static String process(String s) {
    return s.replaceAll("\\{\\{RANDOM}}", Identifier.random().toString());
  }
}
