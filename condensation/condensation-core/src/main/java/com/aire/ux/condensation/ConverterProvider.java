package com.aire.ux.condensation;

import io.sunshower.lang.tuple.Pair;
import java.util.function.Function;

public interface ConverterProvider<T, U> {

  Function<T, U> getConverter();

  Pair<Class<T>, Class<U>> getTypeMapping();
}
