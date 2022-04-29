package io.sunshower.arcus.config;

public interface ConfigurationWriter {

  <T> void write(Configuration<T> configuration);

}
