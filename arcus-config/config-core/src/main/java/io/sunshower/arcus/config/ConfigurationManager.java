package io.sunshower.arcus.config;

import java.io.InputStream;

public interface ConfigurationManager {

  <T> Configuration<T> read(Class<T> type, InputStream source, String name);
}
