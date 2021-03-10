package io.sunshower.persist.core;

import com.zaxxer.hikari.HikariConfig;

public class DataSourceConfigurations {

  public static HikariConfig toNative(DatabaseConfigurationSource source) {
    final HikariConfig cfg = new HikariConfig();
    cfg.setJdbcUrl(source.url());
    cfg.setUsername(source.username());
    cfg.setPassword(source.password());
    cfg.setDriverClassName(source.driverClass());
    return cfg;
  }

  static boolean isBaseline(DatabaseConfigurationSource source) {
    String version = source.version();
    return source.baseline()
        && !(version == null || version.trim().isEmpty() || "-1".equals(version));
  }

  static boolean useLocation(DatabaseConfigurationSource source) {
    return nullOrEmpty(source.jndiPath());
  }

  public static boolean nullOrEmpty(String value) {
    if (value == null || value.trim().equals("") || value.trim().toLowerCase().equals("empty")) {
      return true;
    }
    return false;
  }

  public static void validate(DatabaseConfigurationSource source) {
    if (!(nullOrEmpty(source.jndiPath()) || nullOrEmpty(source.username()))) {
      throw new IllegalStateException("Only one of jndi-path or username may be set");
    }
  }

  public static boolean isBaselineVersion(DatabaseConfigurationSource source) {
    String version = source.version();
    return source.baseline()
        && !(version == null || version.trim().isEmpty() || "-1".equals(version));
  }
}
