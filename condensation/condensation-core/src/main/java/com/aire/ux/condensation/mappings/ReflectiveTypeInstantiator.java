package com.aire.ux.condensation.mappings;

import com.aire.ux.condensation.TypeInstantiator;
import io.sunshower.arcus.reflect.Reflect;
import io.sunshower.lang.tuple.Pair;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class ReflectiveTypeInstantiator implements TypeInstantiator {

  private final Map<Class<?>, Supplier<?>> registeredSuppliers;

  public ReflectiveTypeInstantiator() {
    this.registeredSuppliers = new HashMap<>();
  }

  @Override
  public <T> boolean canInstantiate(Class<T> type) {
    return true;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T instantiate(Class<T> type, Pair<Class<?>, Object>... args) {
    if (Map.class.isAssignableFrom(type)) {
      return (T) new LinkedHashMap<>();
    } else if (List.class.isAssignableFrom(type)) {
      return (T) new ArrayList<>();
    }
    return Optional.ofNullable((Supplier<T>) registeredSuppliers.get(type))
        .orElse(() -> Reflect.instantiate(type, args))
        .get();
  }

  public <T> void register(Class<T> type, Supplier<T> supplier) {
    registeredSuppliers.put(type, supplier);
  }
}
