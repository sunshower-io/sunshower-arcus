package io.sunshower.arcus.config.condensation;

import io.sunshower.arcus.condensation.RootElement;
import io.sunshower.arcus.config.ConfigurationMapping;
import io.sunshower.arcus.reflect.HierarchyTraversalMode;
import io.sunshower.arcus.reflect.Reflect;
import io.sunshower.lang.tuple.Pair;
import java.util.ArrayDeque;
import java.util.Optional;
import lombok.NonNull;
import lombok.val;

@RootElement
public class ArcusConfigurationObject<T> implements ConfigurationMapping<T> {

  private final String key;
  private final @NonNull T value;
  private final @NonNull Class<T> type;

  public ArcusConfigurationObject(@NonNull Class<T> type, @NonNull T value, @NonNull String key) {
    this.key = key;
    this.type = type;
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
  public <U> @NonNull Optional<U> getValue(@NonNull String path, @NonNull Class<U> targetType) {
    val pathsegments = path.split("\\.");
    val stack = new ArrayDeque<Class<?>>();
    stack.push(type);
    int count = 0;
    Object host = value;
    while (!stack.isEmpty() && count < pathsegments.length) {
      val current = stack.pop();
      val next = searchFor(pathsegments[count], current, host);
      if (next.isPresent()) {
        val n = next.get();
        host = n.snd;
        stack.push(n.fst);
      }
    }
    return Optional.ofNullable((U) host);


  }

  @Override
  public <U> @NonNull void setValue(@NonNull String path, @NonNull U value) {

  }

  @Override
  public @NonNull <U> Optional<U> getValue(String s) {
    return Optional.empty();
  }

  private Optional<
      Pair<
          Class<?>,
          Object
          >
      > searchFor(String key, Class<?> type, Object host) {

    Reflect.hierarchyOf(type, HierarchyTraversalMode.LinearSupertypes);
    return null;

  }
}
