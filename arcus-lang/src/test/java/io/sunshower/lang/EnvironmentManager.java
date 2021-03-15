package io.sunshower.lang;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.function.Consumer;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnvironmentManager {

  static final Logger logger = LogManager.getLogger(EnvironmentManager.class);

  public static PropertiesBuilder props() {
    return new PropertiesBuilder();
  }

  public static void withSystemProperties(
      Map<String, String> values, Consumer<Environment> environment) {
    val properties = System.getProperties();
    try {
      val props = copyToProperties(values);
      try {
        setFieldValue(System.class, null, "props", props);
        environment.accept(Environment.getDefault());
      } catch (ReflectiveOperationException ex) {
        logger.catching(ex);
        throw new RuntimeException(ex);
      }
    } finally {
      try {
        setFieldValue(System.class, null, "props", properties);
      } catch (Exception ex) {
        logger.catching(ex);
      }
    }
  }

  public static void withEnvironment(
      Map<String, String> replacement, Consumer<Environment> environment) {

    AccessController.doPrivileged(
        (PrivilegedAction<Object>) () -> runUnconditionally(replacement, environment));
  }

  public static EnvironmentBuilder env() {
    return new EnvironmentBuilder();
  }

  public static EnvironmentBuilderValueView env(String key) {
    return env().key(key);
  }

  /** we're in a privileged context now */
  private static Object runUnconditionally(
      Map<String, String> replacement, Consumer<Environment> environment) {

    try {

      val os = OperatingSystems.getCurrent();
      switch (os) {
        case Windows:
          runInProcessEnvironment(replacement, environment);
          break;
        default:
          runInSystemEnvironmentClass(replacement, environment);
      }
    } catch (Exception ex) {
      logger.catching(ex);
    }
    return null;
  }

  private static void runInSystemEnvironmentClass(
      Map<String, String> replacement, Consumer<Environment> environment)
      throws ReflectiveOperationException {

    val env = System.getenv();
    Map<String, String> result = null;
    try {
      result = getFieldValue(env.getClass(), env, "m");
      setFieldValue(env.getClass(), env, "m", replacement);
      environment.accept(Environment.getDefault());
    } finally {
      if (result != null) {
        setFieldValue(env.getClass(), env, "m", result);
      }
    }
  }

  private static void runInProcessEnvironment(
      Map<String, String> replacement, Consumer<Environment> environment)
      throws ReflectiveOperationException {
    Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
    Map<String, String> theEnvironment =
        getFieldValue(processEnvironmentClass, null, "theEnvironment");
    Map<String, String> theCaseInsensitiveEnvironment =
        getFieldValue(processEnvironmentClass, null, "theCaseInsensitiveEnvironment");

    try {
      setFieldValue(processEnvironmentClass, null, "theEnvironment", replacement);
      setFieldValue(processEnvironmentClass, null, "theCaseInsensitiveEnvironment", replacement);
      environment.accept(Environment.getDefault());
    } finally {
      setFieldValue(processEnvironmentClass, null, "theEnvironment", theEnvironment);
      setFieldValue(
          processEnvironmentClass,
          null,
          "theCaseInsensitiveEnvironment",
          theCaseInsensitiveEnvironment);
    }
  }

  @SuppressWarnings("unchecked")
  private static void setFieldValue(Class<?> clazz, Object object, String name, Object value)
      throws ReflectiveOperationException {
    Field field = clazz.getDeclaredField(name);
    field.setAccessible(true);
    field.set(object, value);
  }

  @SuppressWarnings("unchecked")
  private static Map<String, String> getFieldValue(Class<?> clazz, Object object, String name)
      throws ReflectiveOperationException {
    Field field = clazz.getDeclaredField(name);
    field.setAccessible(true);
    return (Map<String, String>) field.get(object);
  }

  private static Properties copyToProperties(Map<String, String> values) {
    val properties = new Properties(values.size());
    for (val kv : values.entrySet()) {
      properties.setProperty(kv.getKey(), kv.getValue());
    }
    return properties;
  }

  public static class PropertiesBuilder {

    final Map<String, String> values;

    public PropertiesBuilder() {
      values = new HashMap<>();
    }

    public PropertiesValueView key(String key) {
      return new PropertiesValueView(this, key);
    }

    public void with(Consumer<Environment> environment) {
      withSystemProperties(values, environment);
    }
  }

  public static class PropertiesValueView {

    private final String key;
    private final PropertiesBuilder builder;

    PropertiesValueView(PropertiesBuilder builder, String key) {
      this.key = key;
      this.builder = builder;
    }

    public PropertiesBuilder value(String value) {
      builder.values.put(key, value);
      return builder;
    }
  }

  public static class EnvironmentBuilderValueView {

    final String key;
    final EnvironmentBuilder builder;

    EnvironmentBuilderValueView(final EnvironmentBuilder builder, final String key) {
      this.key = key;
      this.builder = builder;
    }

    public EnvironmentBuilder value(String value) {
      builder.environment.put(key, value);
      return builder;
    }
  }

  public static class EnvironmentBuilder {

    final Map<String, String> environment;

    EnvironmentBuilder() {
      environment = new HashMap<>();
    }

    public EnvironmentBuilderValueView key(String key) {
      return new EnvironmentBuilderValueView(this, key);
    }

    public void with(Consumer<Environment> environmentConsumer) {
      withEnvironment(environment, environmentConsumer);
    }
  }
}
