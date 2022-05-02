package io.sunshower.arcus.config;


public final class ConfigurationDescriptor<T> {

  private final String name;

  private final Class<T> type;


  public ConfigurationDescriptor(String name, Class<T> type) {
    this.name = name;
    this.type = type;
  }
}
