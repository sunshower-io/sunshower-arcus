package io.sunshower.lang;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/** wrapper around the environment */
public interface Environment {

  static Environment getDefault() {
    return Holder.INSTANCE;
  }

  /**
   * @param key the property key
   * @param classLoader the classloader to search for environment implementations
   * @return the property value, or null if it does not exist
   */
  String getSystemProperty(@Nonnull ClassLoader classLoader, String key);

  default String getSystemProperty(String key) {
    return getSystemProperty(Thread.currentThread().getContextClassLoader(), key);
  }

  /**
   * @param key the key for the system property
   * @param defaultValue the default value if the variable does not exist
   * @return the value associated with the key, or defaultValue if it does not exist
   */
  default String getSystemProperty(String key, String defaultValue) {
    return getSystemProperty(Thread.currentThread().getContextClassLoader(), key, defaultValue);
  }

  /**
   * @param key the key for the environment variable
   * @param defaultValue the default value if the variable does not exist
   * @param classLoader the classloader to search for environment implementations
   * @return the value associated with the key, or defaultValue if it does not exist
   */
  String getSystemProperty(
      @Nonnull ClassLoader classLoader, String key, @Nullable String defaultValue);

  /**
   * @param key the key for the environment variable
   * @param classLoader the classloader to search for environment implementations
   * @param defaultValue the value to return if it does not exist
   * @return the value, the default value
   */
  String getEnvironmentVariable(@Nonnull ClassLoader classLoader, String key, String defaultValue);

  /**
   * @param key the environment variable key
   * @param defaultValue the default value
   * @return the value associated with the key or the default value
   */
  default String getEnvironmentVariable(String key, String defaultValue) {
    return getEnvironmentVariable(
        Thread.currentThread().getContextClassLoader(), key, defaultValue);
  }

  default String getEnvironmentVariable(String key) {
    return getEnvironmentVariable(key, null);
  }

  final class Holder {

    static final Environment INSTANCE = new DefaultDelegatingEnvironment();
  }
}
