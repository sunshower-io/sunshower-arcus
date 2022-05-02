package io.sunshower.arcus.config;


import java.io.IOException;

public interface ConfigurationManager extends AutoCloseable {


  /**
   * read all of the configurations supplied by this configuration manager
   * @throws IOException if an exception is encountered reading configurations
   */
  void read() throws IOException;


  /**
   * write all of the configurations present in this configuration manager
   * @throws IOException if any issues are encountered writing configurations
   */
  void save() throws IOException;


  /**
   *
   * @param configurationMapping the configuration to flush
   * @param <T> the type of the configuration
   * @throws IOException if an issue is encountered
   */
  <T> void flushConfiguration(ConfigurationMapping<T> configurationMapping) throws IOException;

  /**
   * write, then re-read the provided configuration to the underlying datastore
   * @param configurationMapping the configuration to reload
   * @param <T> the type of the configuration
   * @return the refreshed configuration
   */
  <T> ConfigurationMapping<T> reload(ConfigurationMapping<T> configurationMapping);

  /**
   * write, then re-read the provided configuration to the underlying datastore
   * @param key the key of the configuration to reload
   * @param <T> the type parameter of the configuration
   * @return the refreshed configuration
   */
  <T> ConfigurationMapping<T> reload(String key);

  /**
   * write, then reload all the configurations associated with the provided keys
   * @param keys the keys of the configurations to refresh.  The reloaded configurations
   *             will be available via {@code getConfiguration}
   * @throws IOException if an issue is encountered
   */
  void flushConfigurations(String...keys) throws IOException;

  /**
   * write the provided configuration
   * @param configurations the configuration
   * @throws IOException if an issue is encountered
   */
  void flushConfigurations(ConfigurationMapping<?> configurations) throws IOException;

  /**
   * retrieve a configuration by name
   * @param name the name of the configuration to get
   * @param <T> the type of the configuration
   * @return the configuration
   */
  <T> ConfigurationMapping<T> getConfiguration(String name);
  <T> ConfigurationMapping<T> getConfiguration(Class<T> type);

  /**
   * return the configuration associated with the specified type.
   * @param type
   * @param name
   * @param <T>
   * @return
   */
  <T> ConfigurationMapping<T> getConfiguration(Class<T> type, String name);


  /**
   * register the specified configuration with this manager
   * @param configurationMapping the configuration to add
   * @param <T>
   */
  <T> void addConfiguration(ConfigurationMapping<T> configurationMapping);


  /**
   * unregister the specified configuration. This operation should delete it from
   * the backing store
   * @param name the name of the configuration to remove
   * @param <T>
   * @return
   */
  <T> ConfigurationMapping<T> removeConfiguration(String name);



}
