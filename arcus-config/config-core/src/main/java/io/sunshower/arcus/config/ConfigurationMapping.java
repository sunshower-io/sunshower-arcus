package io.sunshower.arcus.config;

import java.util.Optional;
import lombok.NonNull;

public interface ConfigurationMapping<T> {

  T get();

  String getKey();

  String getFormat();

  @NonNull <U> Optional<U> getValue(@NonNull String path, @NonNull Class<U> type);

  @NonNull <U> void setValue(@NonNull String path, @NonNull U value);


  @NonNull
  <U> Optional<U> getValue(String s);
}
