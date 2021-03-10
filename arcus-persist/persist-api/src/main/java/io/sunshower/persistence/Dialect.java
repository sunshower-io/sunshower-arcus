package io.sunshower.persistence;

public enum Dialect {
  H2("h2"),
  Postgres("postgres");

  final String key;

  Dialect(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }
}
