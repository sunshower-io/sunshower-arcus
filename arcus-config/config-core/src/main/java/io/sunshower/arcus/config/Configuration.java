package io.sunshower.arcus.config;

import java.util.Optional;
import lombok.NonNull;

public interface Configuration<T> {

  T get();

  String getKey();

  @NonNull <U> Optional<U> getValue(@NonNull String path, @NonNull Class<U> type);

}
