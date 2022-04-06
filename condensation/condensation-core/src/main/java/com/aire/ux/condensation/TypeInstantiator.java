package com.aire.ux.condensation;

import io.sunshower.lang.tuple.Pair;

public interface TypeInstantiator {

  <T> boolean canInstantiate(Class<T> type);

  <T> T instantiate(Class<T> type, Pair<Class<?>, Object>... args);
}
