package io.sunshower.arcus.config;

import java.util.List;
import java.util.Set;

public interface ConfigurationProvider {

  Set<ConfigurationReader> getReaders();

  Set<ConfigurationReader> getReadersForFormat(String format);


  Set<ConfigurationWriter> getWriters();

  Set<ConfigurationWriter> getWritersForFormat(String format);

  List<ConfigurationDescriptor<?>> getDescriptors();
}
