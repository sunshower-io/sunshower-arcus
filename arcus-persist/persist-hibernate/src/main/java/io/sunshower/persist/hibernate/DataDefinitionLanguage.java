package io.sunshower.persist.hibernate;

public interface DataDefinitionLanguage {
  default String strategy() {
    return "none";
  }

  boolean generate();

  boolean showSql();

  boolean formatSql();
}
