package io.sunshower.persistence;

import java.util.*;
import lombok.val;

public class PersistenceConfiguration implements Comparable<PersistenceConfiguration> {
  final Integer order;
  final String id;
  final String schema;
  final Set<String> migrationPaths;
  final Set<String> packagesToScan;
  final Set<Class<?>> entityTypes;

  public PersistenceConfiguration(
      String id,
      String schema,
      Integer order,
      Collection<String> mpaths,
      Collection<String> pnames,
      Collection<Class<?>> etypes) {
    this.id = id;
    this.order = order;
    this.schema = schema;
    this.entityTypes = new HashSet<>(etypes);
    this.migrationPaths = new HashSet<>(mpaths);
    this.packagesToScan = new HashSet<>(pnames);
  }

  public PersistenceConfiguration merge(PersistenceConfiguration cfg) {
    migrationPaths.addAll(cfg.migrationPaths);
    packagesToScan.addAll(cfg.packagesToScan);
    entityTypes.addAll(cfg.entityTypes);
    return this;
  }

  public String getSchema() {
    return schema;
  }

  public String getId() {
    return id;
  }

  public String[] getMigrationPaths() {
    return migrationPaths.toArray(new String[migrationPaths.size()]);
  }

  public String[] getPackagesToScan() {
    return packagesToScan.toArray(new String[packagesToScan.size()]);
  }

  public Class<?>[] getEntityTypes() {
    return entityTypes.toArray(new Class[entityTypes.size()]);
  }

  @Override
  public int compareTo(PersistenceConfiguration o) {
    if (o == null) {
      return 1;
    }
    val oeq = order.compareTo(o.order);
    if (oeq == 0) {
      return id.compareTo(o.id);
    }
    return oeq;
  }
}
