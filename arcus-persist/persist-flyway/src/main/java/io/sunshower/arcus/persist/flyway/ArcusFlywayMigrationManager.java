package io.sunshower.arcus.persist.flyway;

import io.sunshower.persistence.MigrationManager;
import io.sunshower.persistence.config.DataSourceConfiguration;
import javax.sql.DataSource;
import lombok.val;
import org.flywaydb.core.Flyway;

public class ArcusFlywayMigrationManager implements MigrationManager {

  private final DataSourceConfiguration configuration;

  public ArcusFlywayMigrationManager(DataSourceConfiguration configuration) {
    this.configuration = configuration;
  }

  @Override
  public void apply(DataSource dataSource) {
    val flyway = createFlywayFrom(configuration, dataSource);
    flyway.migrate();
  }

  @Override
  public void unapply(DataSource dataSource) {
    val flyway = createFlywayFrom(configuration, dataSource);
    flyway.undo();
  }

  private Flyway createFlywayFrom(DataSourceConfiguration configuration, DataSource dataSource) {
    return Flyway.configure(Thread.currentThread().getContextClassLoader())
        .dataSource(dataSource)
        .batch(true)
        .locations(configuration.getMigrationLocations().toArray(new String[0]))
        .createSchemas(true)
        .load();
  }
}
