package io.sunshower.lang;

import java.util.ServiceLoader;
import lombok.val;

public class DefaultDelegatingEnvironment implements Environment {

  @Override
  public String getSystemProperty(ClassLoader classLoader, String key) {
    return getSystemProperty(classLoader, key, null);
  }

  @Override
  public String getSystemProperty(ClassLoader classLoader, String key, String defaultValue) {
    for (val environment : ServiceLoader.load(Environment.class, classLoader)) {
      val result = environment.getSystemProperty(classLoader, key);
      if (result != null) {
        return result;
      }
    }
    return System.getProperty(key, defaultValue);
  }

  @Override
  public String getEnvironmentVariable(ClassLoader classLoader, String key, String defaultValue) {
    for (val environment : ServiceLoader.load(Environment.class, classLoader)) {
      val result = environment.getEnvironmentVariable(classLoader, key, defaultValue);
      if (result != null) {
        return result;
      }
    }
    val result = System.getenv(key);
    if (result != null) {
      return result;
    }

    return defaultValue;
  }
}
