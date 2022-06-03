package io.sunshower.persistence;

import javax.sql.DataSource;

public interface MigrationManager {

  void apply(DataSource dataSource);

  void unapply(DataSource dataSource);
}
