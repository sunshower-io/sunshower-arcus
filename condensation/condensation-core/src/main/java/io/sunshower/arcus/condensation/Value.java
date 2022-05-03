package io.sunshower.arcus.condensation;

public interface Value<T, E extends Enum<E>> {

  E getType();

  T getValue();

  boolean isScalar();
}
