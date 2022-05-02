package io.sunshower.arcus.config.condensation;

import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.arcus.config.ConfigurationMapping;
import io.sunshower.arcus.config.ConfigurationManager;
import io.sunshower.arcus.config.ConfigurationProvider;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.concurrent.ThreadSafe;
import lombok.NonNull;

@ThreadSafe
public class ArcusConfigurationManager implements ConfigurationManager {


  private final Map<String, ConfigurationMapping<?>> configurationsByName;
  private final Map<Class<?>, ConfigurationMapping<?>> configurationsByType;

  private final Condensation condensation;
  private final ConfigurationProvider configurationProvider;

  public ArcusConfigurationManager(@NonNull Condensation condensation,
      @NonNull ConfigurationProvider configurationProvider) {
    this.condensation = condensation;
    this.configurationProvider = configurationProvider;
    this.configurationsByName = new HashMap<>();
    this.configurationsByType = new HashMap<>();
  }



  @Override
  public void read() throws IOException {

  }

  @Override
  public void save() throws IOException {

  }

  @Override
  public <T> void flushConfiguration(ConfigurationMapping<T> configurationMapping) throws IOException {

  }

  @Override
  public <T> ConfigurationMapping<T> reload(ConfigurationMapping<T> configurationMapping) {
    return null;
  }

  @Override
  public <T> ConfigurationMapping<T> reload(String key) {
    return null;
  }

  @Override
  public void flushConfigurations(String... keys) throws IOException {

  }

  @Override
  public void flushConfigurations(ConfigurationMapping<?> configurations) throws IOException {

  }

  @Override
  public <T> ConfigurationMapping<T> getConfiguration(String name) {
    return null;
  }

  @Override
  public <T> ConfigurationMapping<T> getConfiguration(Class<T> type) {
    return null;
  }

  @Override
  public <T> ConfigurationMapping<T> getConfiguration(Class<T> type, String name) {
    return null;
  }

  @Override
  public <T> void addConfiguration(ConfigurationMapping<T> configurationMapping) {

  }

  @Override
  public <T> ConfigurationMapping<T> removeConfiguration(String name) {
    return null;
  }

  @Override
  public void close() throws Exception {

  }
}
