package io.sunshower.persistence.config;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import lombok.val;

public interface DataSourceConfiguration {

  static List<DataSourceConfiguration> load(URL location) throws IOException {
    try (val stream = location.openStream()) {
      return ServiceLoader.load(DataSourceConfigurationReader.class, Thread.currentThread()
              .getContextClassLoader()).stream()
          .map(Provider::get)
          .filter(p -> p.handles(location))
          .map(p -> p.read(stream)).findAny()
          .orElseThrow(() -> new NoSuchElementException(
              String.format("No provider found for url '%s'", location)));
    }
  }

  String getConnectionString();

  String[] getScannedPackages();

  String getDriverClassName();

  String getUsername();

  String getPassword();

  Mode getMode();


  Map<String, String> getAdditionalProperties();

  enum Mode {
    ReadOnly,
    WriteOnly,
    Both
  }
}
