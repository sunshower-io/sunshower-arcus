package io.sunshower.persistence;

public class MigrationResult {

  private final PersistenceUnit context;

  public MigrationResult(PersistenceUnit context) {
    this.context = context;
  }
}
