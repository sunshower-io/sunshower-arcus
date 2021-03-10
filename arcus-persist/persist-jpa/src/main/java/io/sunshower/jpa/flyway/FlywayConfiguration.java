package io.sunshower.jpa.flyway;

import static io.sunshower.persist.core.DataSourceConfigurations.isBaselineVersion;

import io.sunshower.persist.core.DatabaseConfigurationSource;
import io.sunshower.persistence.Dialect;
import io.sunshower.persistence.MigrationResult;
import io.sunshower.persistence.PersistenceConfiguration;
import io.sunshower.persistence.PersistenceUnit;
import javax.inject.Singleton;
import javax.sql.DataSource;
import lombok.val;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {
  static final Logger log = LoggerFactory.getLogger(FlywayConfiguration.class);

  @Bean
  public static PersistenceUnit persistenceConfigurationProcessor(
      ApplicationContext context, Dialect dialect) {
    log.info("Configured with dialect: {}", dialect.getKey());
    return new PersistenceUnit(dialect, context);
  }

  @Bean
  @Singleton
  public MigrationResult createMigrations(
      DataSource dataSource, PersistenceUnit context, DatabaseConfigurationSource source) {
    for (PersistenceConfiguration ctx : context.configurations()) {
      val flyway = Flyway.configure();

      if (isBaselineVersion(source)) {
        log.info("Setting baseline version to {}", source.version());
        flyway.baselineVersion(source.version());
      }
      final String table = ctx.getId() + "_migrations";
      flyway.table(table);
      if (!ctx.getSchema().trim().isEmpty()) {
        flyway.schemas(ctx.getSchema());
      }
      flyway.dataSource(dataSource);
      String[] migrationPaths = ctx.getMigrationPaths();
      flyway.locations(migrationPaths);
      val fway = flyway.load();
      if (isBaselineVersion(source)) {
        log.info("baselining database...");
        fway.baseline();
        log.info("database baselined");
      }
      fway.migrate();
    }
    return new MigrationResult(context);
  }
}
