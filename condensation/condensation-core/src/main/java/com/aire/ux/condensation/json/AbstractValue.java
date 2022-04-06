package com.aire.ux.condensation.json;

import javax.annotation.Nonnull;

public class AbstractValue<T> implements Value<T> {
  final T value;
  final Type type;

  public AbstractValue(final Type type, final T value) {
    this.type = type;
    this.value = value;
  }

  @Nonnull
  @Override
  public Type getType() {
    return type;
  }

  @Override
  public T getValue() {
    return value;
  }

  public String toString() {
    return String.format("%s::%s", value, type);
  }
}
