package io.sunshower.persist.hibernate;

public interface SearchConfiguration {

  default String type() {
    return "hibernate.search.default.directory_provider";
  }

  String value();
}
