package com.aire.ux.condensation.mappings;

import com.aire.ux.condensation.TypeInstantiator;
import io.sunshower.arcus.reflect.InstantiationException;
import io.sunshower.lang.tuple.Pair;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.val;

public class CachingDelegatingTypeInstantiator implements TypeInstantiator {

  static final int DEFAULT_CACHE_SIZE = 100;

  private final List<TypeInstantiator> delegates;

  private final Map<Class<?>, TypeInstantiator> instantiatorCache;

  public CachingDelegatingTypeInstantiator(TypeInstantiator... delegates) {
    this(DEFAULT_CACHE_SIZE, delegates);
  }

  public CachingDelegatingTypeInstantiator(final int cacheSize, TypeInstantiator... delegates) {
    this.delegates = List.of(delegates);
    instantiatorCache =
        new LinkedHashMap<>() {
          @Override
          protected boolean removeEldestEntry(Entry<Class<?>, TypeInstantiator> eldest) {
            return size() >= cacheSize;
          }
        };
  }

  @Override
  public <T> boolean canInstantiate(Class<T> type) {
    if (instantiatorCache.containsKey(type)) {
      return true;
    }
    for (val delegate : delegates) {
      if (delegate.canInstantiate(type)) {
        instantiatorCache.put(type, delegate);
        return true;
      }
    }
    return false;
  }

  @Override
  public <T> T instantiate(Class<T> type, Pair<Class<?>, Object>... args) {
    if (canInstantiate(type)) {
      return instantiatorCache.get(type).instantiate(type, args);
    }
    throw new InstantiationException(
        String.format("No instantiator available for type '%s', args: '%s'", type, List.of(args)));
  }
}
