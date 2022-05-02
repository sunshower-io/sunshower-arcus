package io.sunshower.arcus.config;

import java.util.Optional;
import lombok.NonNull;

public class DefaultConfiguration<T> implements ConfigurationMapping<T> {

  private final T value;
  private final String key;

  public DefaultConfiguration(final String key, final T value) {
    this.key = key;
    this.value = value;

  }

  @Override
  public T get() {
    return value;
  }

  @Override
  public String getKey() {
    return key;
  }

  @Override
  public String getFormat() {
    return null;
  }

  @Override
  public @NonNull <U> Optional<U> getValue(@NonNull String path, @NonNull Class<U> type) {
    return Optional.empty();
  }

  @Override
  public <U> @NonNull void setValue(@NonNull String path, @NonNull U value) {

  }

  @Override
  public @NonNull <U> Optional<U> getValue(String s) {
    return Optional.empty();
  }
}
