package io.sunshower.arcus.config;

import io.sunshower.lang.io.Files;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * ConfigurationLoader attempts to resolve implementations of ConfigurationReaders which are then
 * used to load configuration values into instances
 */
public class ConfigurationLoader {

  static final Logger log = LogManager.getLogger(ConfigurationLoader.class);

  /**
   * @param type the type to map the configuration file to
   * @param file the file to try to load from
   * @param <T> the type-parameter of the configuration type
   * @return an instance of T populated by the configuration parameters according to the
   *     first-matched ConfigurationReader
   * @throws Exception if the file can't be found, or if the file's contents are invalid, or if the
   *     mime-type of the file couldn't be determined
   */
  public static <T> T load(Class<T> type, File file) throws Exception {
    return load(
        type,
        file,
        detectMimeType(Thread.currentThread().getContextClassLoader(), file.getAbsolutePath()));
  }

  /**
   * @param type the type to map the configuration file to
   * @param file the file to try to load from
   * @param <T> the type-parameter of the configuration type
   * @param classLoader the classloader to search for ConfigurationReaders
   * @return an instance of T populated by the configuration parameters according to the
   *     first-matched ConfigurationReader
   * @throws Exception if the file can't be found, or if the file's contents are invalid, or if the
   *     mime-type of the file couldn't be determined
   */
  public static <T> T load(ClassLoader classLoader, Class<T> type, File file) throws Exception {
    return load(type, file, detectMimeType(classLoader, file.getAbsolutePath()));
  }

  /**
   * @param type the type to map the configuration file to
   * @param file the file to try to load from
   * @param <T> the type-parameter of the configuration type
   * @param mimetype the mime-type of the content to load.
   * @return an instance of T populated by the configuration parameters according to the
   *     first-matched ConfigurationReader
   * @throws Exception if the file can't be found, or if the file's contents are invalid, or if the
   *     mime-type of the file couldn't be determined
   */
  public static <T> T load(Class<T> type, File file, String mimetype) throws Exception {
    try (val reader = new FileReader(file, StandardCharsets.UTF_8)) {
      return load(type, reader, mimetype);
    }
  }

  /**
   * @param classLoader the classloader to search
   * @param type the type to attempt to loader
   * @param reader the reader to read from
   * @param extension the extension to locate a mime-type for
   * @param <T> the type-parameter of the configuration
   * @return the populated configuration
   */
  public static <T> T loadByExtension(
      ClassLoader classLoader, Class<T> type, Reader reader, String extension) throws Exception {
    val mimetype = detectMimeType(classLoader, "test." + extension);
    return load(classLoader, type, reader, mimetype);
  }

  /**
   * @param type the type to map the configuration file to
   * @param reader a reader backed by the content of the configuration. This method does not close
   *     the the reader when complete
   * @param <T> the type-parameter of the configuration type
   * @param mimetype the mime-type of the content to load.
   * @return an instance of T populated by the configuration parameters according to the
   *     first-matched ConfigurationReader
   * @throws Exception if the file can't be found, or if the file's contents are invalid, or if the
   *     mime-type of the file couldn't be determined
   */
  public static <T> T load(Class<T> type, @WillNotClose Reader reader, String mimetype)
      throws Exception {
    return load(Thread.currentThread().getContextClassLoader(), type, reader, mimetype);
  }

  /**
   * @param type the type to map the configuration file to
   * @param reader a reader backed by the content of the configuration. This method does not close
   *     the the reader when complete
   * @param <T> the type-parameter of the configuration type
   * @param mimeType the mime-type of the content to load.
   * @param classLoader the classloader to attempt to load ConfigurationLoaders from
   * @return an instance of T populated by the configuration parameters according to the
   *     first-matched ConfigurationReader
   * @throws Exception if the file can't be found, or if the file's contents are invalid, or if the
   *     mime-type of the file couldn't be determined
   */
  public static <T> T load(
      ClassLoader classLoader, Class<T> type, @WillNotClose Reader reader, String mimeType)
      throws Exception {
    log.info("Attempting to load reader for format '{}' (type={})", mimeType, type);
    val loader = ServiceLoader.load(ConfigurationReader.class, classLoader).iterator();
    while (loader.hasNext()) {
      val next = loader.next();
      log.info("Located configuration reader: {} ", next);
      if (next.handles(mimeType)) {
        log.info("Successfully located configuration reader applicable to {}: {}", mimeType, next);
        return next.read(type, reader);
      } else {
        log.info("Reader {} was not applicable to {}", next, mimeType);
      }
    }
    log.error("Failed to locate any reader for format: {}", mimeType);
    throw new NoSuchElementException("Failed to resolve service of type: ");
  }

  /**
   * search the current thread's context classloader for supported configurations
   *
   * @return a set (possibly empty) of supported file extensions. The returned file-extensions do
   *     not contain the extension separator (.)
   */
  public static Set<String> detectSupportedConfigurationFormats() {
    return detectSupportedConfigurationFormats(Thread.currentThread().getContextClassLoader());
  }

  /**
   * @param classLoader the classloader to search for ConfigurationReaders
   * @return a set (possibly empty) of supported file-types
   */
  @Nonnull
  public static Set<String> detectSupportedConfigurationFormats(ClassLoader classLoader) {
    val result = new HashSet<String>();
    for (val reader : ServiceLoader.load(ConfigurationReader.class, classLoader)) {
      for (val fileType : reader.knownFileTypes()) {
        result.add(fileType.fst);
      }
    }
    return result;
  }

  static String detectMimeType(ClassLoader classLoader, String path) {
    log.info("Attempting to detect mime-type from path: '{}'", path);

    val mimetype = URLConnection.getFileNameMap().getContentTypeFor(path);
    if (mimetype != null) {
      log.info("Successfully detected mime-type '{}' for path '{}'", mimetype, path);
      return mimetype;
    }

    val extension =
        Files.getExtension(path)
            .orElseThrow(
                () -> {
                  log.info(
                      "Path '{}' has no extension--cannot determine the mime-type from it", path);
                  throw new NoSuchElementException("Unable to locate extension for " + path);
                });
    for (val reader : ServiceLoader.load(ConfigurationReader.class, classLoader)) {
      val knownTypes = reader.knownFileTypes();
      for (val types : knownTypes) {
        if (types.fst.equals(extension)) {
          log.info(
              "Successfully resolved mime-type '{}' for path: {} (extension: '{}')",
              types.snd,
              path,
              extension);
          return types.snd;
        }
      }
    }
    throw new NoSuchElementException("Could not locate handler for file " + path);
  }
}
