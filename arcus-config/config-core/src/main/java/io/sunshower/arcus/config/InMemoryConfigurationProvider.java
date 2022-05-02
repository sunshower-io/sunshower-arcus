package io.sunshower.arcus.config;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class InMemoryConfigurationProvider implements ConfigurationProvider {


  public <T> void registerConfiguration(
      Class<T> type, Supplier<InputStream> inputStream,
      Supplier<OutputStream> outputStream, String format) {

  }

  @Override
  public Set<ConfigurationReader> getReaders() {
    return null;
  }

  @Override
  public Set<ConfigurationReader> getReadersForFormat(String format) {
    return null;
  }

  @Override
  public Set<ConfigurationWriter> getWriters() {
    return null;
  }

  @Override
  public Set<ConfigurationWriter> getWritersForFormat(String format) {
    return null;
  }

  @Override
  public List<ConfigurationDescriptor<?>> getDescriptors() {
    return null;
  }
}
