package com.aire.ux.condensation.converters;

import com.aire.ux.condensation.ConverterProvider;
import io.sunshower.lang.tuple.Pair;
import java.util.function.Function;

public class DoubleToInteger
    implements ConverterProvider<Double, Integer>, Function<Double, Integer> {

  @Override
  public Function<Double, Integer> getConverter() {
    return this;
  }

  @Override
  public Pair<Class<Double>, Class<Integer>> getTypeMapping() {
    return Pair.of(Double.class, Integer.class);
  }

  @Override
  public Integer apply(Double aDouble) {
    return aDouble.intValue();
  }
}
