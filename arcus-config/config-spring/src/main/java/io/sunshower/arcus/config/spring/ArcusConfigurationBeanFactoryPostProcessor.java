package io.sunshower.arcus.config.spring;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.config.ConfigurationException;
import io.sunshower.arcus.config.ConfigurationLoader;
import io.sunshower.arcus.config.Configurations;
import io.sunshower.arcus.config.Configure;
import io.sunshower.arcus.logging.Logging;
import io.sunshower.lang.Environment;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;

public class ArcusConfigurationBeanFactoryPostProcessor
    implements BeanFactoryPostProcessor, AutoCloseable {

  static final Logger log = LogManager.getLogger(ArcusConfigurationBeanFactoryPostProcessor.class);

  static final String DEFAULT_VALUE;
  static final String CLASSLOADER_PREFIX;
  static final String CONFIGURE_CLASS_NAME;
  static final String CONFIGURATIONS_CLASS_NAME;

  static {
    DEFAULT_VALUE = "__default__";
    CLASSLOADER_PREFIX = "configurations";
    CONFIGURE_CLASS_NAME = Configure.class.getName();
    CONFIGURATIONS_CLASS_NAME = Configurations.class.getName();
    Logging.setLevel(ArcusConfigurationBeanFactoryPostProcessor.class, Level.ALL);
  }

  private ClassLoader classLoader;
  private Environment environment;
  private List<String> knownExtensions;

  public ArcusConfigurationBeanFactoryPostProcessor() {
    this(Thread.currentThread().getContextClassLoader());
  }

  public ArcusConfigurationBeanFactoryPostProcessor(ClassLoader classLoader) {
    Objects.requireNonNull(classLoader, "Must provide a classloader");

    this.classLoader = classLoader;
    registerExtensions(classLoader);
  }

  static String snakeCase(String name) {
    val result = new StringBuilder(name.length());

    for (int i = 0; i < name.length(); i++) {
      char ch = name.charAt(i);
      if (Character.isUpperCase(ch)) {
        if (i > 0) {
          result.append("-");
        }
        result.append(Character.toLowerCase(ch));
      } else {
        result.append(ch);
      }
    }
    return result.toString();
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
      throws BeansException {
    log.info("Scanning for configurations...");
    val beanNames = beanFactory.getBeanNamesIterator();

    while (beanNames.hasNext()) {
      process(beanNames.next(), beanFactory);
    }
    log.info("Successfully scanned for configurations");
  }

  /**
   * we need to ensure that we're not holding a reference to a classloader, or that may prevent
   * garbage-collection of this module
   *
   * @throws Exception doesn't actually thrown anything
   */
  @Override
  public void close() throws Exception {
    classLoader = null;
    knownExtensions = null;
  }

  private void process(String beanName, ConfigurableListableBeanFactory beanFactory) {
    log.info("Scanning bean definition named '{}'", beanName);

    if (beanFactory.containsBeanDefinition(beanName)) {
      val definition = beanFactory.getBeanDefinition(beanName);
      if (definition instanceof AnnotatedBeanDefinition def) {
        process(def, beanFactory);
      }
    }
    log.info("Scanned bean definition '{}'", beanName);
  }

  @SuppressFBWarnings
  private void process(AnnotatedBeanDefinition def, ConfigurableListableBeanFactory beanFactory) {
    var metadata = def.getMetadata().getAllAnnotationAttributes(CONFIGURATIONS_CLASS_NAME);
    if (metadata != null) {
      for (val value : metadata.get("value")) {
        if (value instanceof LinkedHashMap<?, ?>[] annotations) {
          for (val annotationHolder : annotations) {
            processConfiguration(annotationHolder, beanFactory);
          }
        }
      }
    }

    var singleMetadata = def.getMetadata().getAnnotationAttributes(CONFIGURE_CLASS_NAME);
    if (singleMetadata instanceof Map annotation) {
      processConfiguration(annotation, beanFactory);
    }
  }

  /**
   * @param annotation the actual configuration class that must be bound to a configuration file we
   *     must check all the available extensions from ConfigurationLoader, then search in
   *     classpath:/configurations/{bean-name:snake-case}.{ext}
   *     <p>we bind the first extension we encounter at the location. If no files with any of the
   *     extensions are encountered, with throw a ConfigurationException and bail
   */
  private void processConfiguration(
      Map<?, ?> annotation, ConfigurableListableBeanFactory beanFactory) {
    val definedName = (String) annotation.get("name");
    val configurationType = (Class<?>) annotation.get("value");
    val actualName = extractName(definedName, configurationType);

    if (beanFactory.containsBean(actualName)) {
      log.error("Error: Configuration duplicated for configuration named {}", actualName);
      throw new ConfigurationException(
          "Error:  Configuration definition duplicated for configuration named '%s'"
              .formatted(actualName));
    }

    val configuration = loadConfiguration(actualName, configurationType);
    if (configuration != null) {
      defineConfiguration(configurationType, configuration, actualName, beanFactory);
    }
  }

  @SuppressWarnings("unchecked")
  private void defineConfiguration(
      Class<?> configurationType,
      Object configuration,
      String actualName,
      ConfigurableListableBeanFactory beanFactory) {
    ((BeanDefinitionRegistry) beanFactory)
        .registerBeanDefinition(
            actualName,
            BeanDefinitionBuilder.genericBeanDefinition(
                    (Class) configurationType, () -> configuration)
                .getBeanDefinition());
  }

  private Object loadConfiguration(String actualName, Class<?> configurationType) {
    if (log.isEnabled(Level.DEBUG)) {
      log.info(
          "Attempting to load configuration named '{}' with extensions: [{}]",
          actualName,
          knownExtensions);
    }
    environment = Environment.getDefault();

    for (val extension : knownExtensions) {
      var configuration = loadFromSystemProperties(extension, actualName, configurationType);

      if (configuration != null) {
        return configuration;
      }

      configuration = loadFromEnvironment(extension, actualName, configurationType);
      if (configuration != null) {
        return configuration;
      }

      configuration = loadFromClassloader(extension, actualName, configurationType);
      if (configuration != null) {
        return configuration;
      }
    }
    return null;
  }

  static String toEnvironmentVariable(String propertyKey) {
    StringBuilder b = new StringBuilder(propertyKey.length()).append("ARCUS_");
    for (int i = 0; i < propertyKey.length(); i++) {
      char ch = propertyKey.charAt(i);
      if(ch == '-') {
        b.append('_');
      } else if (Character.isUpperCase(ch)) {
        if (i > 0) {
          b.append("_").append(ch);
        } else {
          b.append(ch);
        }
      } else {
        b.append(Character.toUpperCase(ch));
      }
    }
    return b.toString();
  }

  private Object loadFromEnvironment(
      String extension, String actualName, Class<?> configurationType) {

    val environmentVariable = toEnvironmentVariable(actualName);
    log.debug("Checking environment for {}", environmentVariable);
    val prop = environment.getEnvironmentVariable(classLoader, environmentVariable);

    if (prop == null) {
      log.info("No environment variable named '{}'", environmentVariable);
      return null;
    }

    val file = new File(prop);

    if (!file.exists()) {
      log.error(
          "Environment variable '{}' specified, but '{}' does not exist",
          environmentVariable,
          prop);
      throw new ConfigurationException("Error: file '%s' does not exist".formatted(prop));
    }

    if (file.isDirectory()) {
      log.error(
          "Expected file for environment variable '{}' but got a directory ({})",
          environmentVariable,
          prop);

      throw new ConfigurationException(
          "Error: Expected file for environment variable '%s' but got a directory (%s)"
              .formatted(environmentVariable, prop));
    }

    checkAccess(file);

    try {
      return ConfigurationLoader.load(configurationType, file);
    } catch (Exception ex) {
      throw createConfigurationError(file.getAbsolutePath(), ex);
    }
  }

  private void checkAccess(File file) {
    if (!file.canRead()) {
      log.error("Error: file '{}' exists, but permission to read it doesn't", file);
      throw new ConfigurationException(
          "Error: file '%s' exists, but permission to read it doesn't".formatted(file));
    }
  }

  private Object loadFromSystemProperties(
      String extension, String actualName, Class<?> configurationType) {

    val expectedProperty = "configuration.%s".formatted(actualName);
    log.debug("Checking system properties for {}", expectedProperty);
    val prop = environment.getSystemProperty(classLoader, expectedProperty);

    if (prop == null) {
      log.info("No system property named '{}'", expectedProperty);
      return null;
    }

    val file = new File(prop);

    if (!file.exists()) {
      log.info(
          "Configuration property '{}' specified, but '{}' does not exist", expectedProperty, prop);
      return null;
    }

    if (file.isDirectory()) {
      log.error(
          "Expected file for configuration property '{}' but got a directory ({})",
          expectedProperty,
          prop);

      throw new ConfigurationException(
          "Error: Expected file for configuration property '%s' but got a directory (%s)"
              .formatted(expectedProperty, prop));
    }

    checkAccess(file);

    try {
      return ConfigurationLoader.load(configurationType, file);
    } catch (Exception ex) {
      throw createConfigurationError(file.getAbsolutePath(), ex);
    }
  }

  private Object loadFromClassloader(
      String extension, String actualName, Class<?> configurationType) {

    val location = "%s/%s.%s".formatted(CLASSLOADER_PREFIX, actualName, extension);
    var resource = classLoader.getResourceAsStream(location);

    if (resource == null) {
      resource = classLoader.getResourceAsStream("/" + location);
    }

    if (resource == null) {
      log.info("No classpath resource for extension '{}' at '{}'", extension, location);
      return null;
    } else {
      return loadConfiguration(extension, configurationType, location, resource);
    }
  }

  private Object loadConfiguration(
      String extension, Class<?> configurationType, String location, java.io.InputStream resource) {
    try (val reader = new InputStreamReader(resource, StandardCharsets.UTF_8)) {
      return ConfigurationLoader.loadByExtension(classLoader, configurationType, reader, extension);
    } catch (Exception e) {
      throw createConfigurationError(location, e);
    }
  }

  private RuntimeException createConfigurationError(String location, Exception e) {
    log.warn(
        "Encountered error '{}' while loading configuration from '{}'", e.getMessage(), location);
    return new ConfigurationException(e);
  }

  private String extractName(String definedName, Class<?> configurationType) {
    log.debug("Processing annotation (type: '{}', name: '{}')", definedName, configurationType);

    final String name;
    if (DEFAULT_VALUE.equals(definedName)) {
      name = snakeCase(configurationType.getSimpleName());
    } else {
      name = definedName;
    }
    log.debug("Configuration name: {}", name);
    return name;
  }

  private void registerExtensions(ClassLoader classLoader) {
    knownExtensions =
        new ArrayList<>(ConfigurationLoader.detectSupportedConfigurationFormats(classLoader));
    Collections.sort(knownExtensions);
    logExtensions();
  }

  private void logExtensions() {
    if (log.isEnabled(Level.DEBUG)) {
      log.debug("Processing files with the following extensions: ");
      for (val ext : knownExtensions) {
        log.debug("\t {}", ext);
      }
    }
  }
}
