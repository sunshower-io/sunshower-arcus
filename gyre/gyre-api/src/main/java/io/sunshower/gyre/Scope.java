package io.sunshower.gyre;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Scope {

  static Scope root() {
    return new RootScope();
  }

  <T> void set(String name, T value);

  <T> T get(String name);

  <E> E computeIfAbsent(String scannedPlugins, E o);
}

final class RootScope implements Scope {

  final Map<String, Object> values = new ConcurrentHashMap<>();

  @Override
  public <T> void set(String name, T value) {
    values.put(name, value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T get(String name) {
    return (T) values.get(name);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T computeIfAbsent(String name, T value) {
    return (T) values.computeIfAbsent(name, t -> value);
  }
}
