package com.aire.ux.condensation.converters;

import com.aire.ux.condensation.ConverterProvider;
import io.sunshower.lang.tuple.Pair;
import java.util.function.Function;

public class IntegerToDouble
    implements ConverterProvider<Integer, Double>, Function<Integer, Double> {

  @Override
  public Function<Integer, Double> getConverter() {
    return this;
  }

  @Override
  public Pair<Class<Integer>, Class<Double>> getTypeMapping() {
    return Pair.of(Integer.class, Double.class);
  }

  @Override
  public Double apply(Integer integer) {
    return integer.doubleValue();
  }
}
