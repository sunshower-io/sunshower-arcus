package io.sunshower.arcus.persist.flyway;

import static java.lang.Thread.currentThread;

import io.sunshower.persistence.MigrationManager;
import io.sunshower.persistence.config.DataSourceConfiguration;
import java.util.function.Supplier;
import javax.sql.DataSource;
import lombok.val;
import org.flywaydb.core.Flyway;

public class ArcusFlywayMigrationManager implements MigrationManager {

  private final DataSourceConfiguration configuration;
  private final Supplier<ClassLoader> classloaderSupplier;

  public ArcusFlywayMigrationManager(
      Supplier<ClassLoader> classLoaderSupplier, DataSourceConfiguration configuration) {
    this.configuration = configuration;
    this.classloaderSupplier = classLoaderSupplier;
  }

  public ArcusFlywayMigrationManager(DataSourceConfiguration configuration) {
    this(() -> currentThread().getContextClassLoader(), configuration);
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
    return Flyway.configure(classloaderSupplier.get())
        .dataSource(dataSource)
        .validateOnMigrate(true)
        .locations(configuration.getMigrationLocations().toArray(new String[0]))
        .createSchemas(true)
        .baselineOnMigrate(true)
        .loggers("auto")
        .load();
  }
}
