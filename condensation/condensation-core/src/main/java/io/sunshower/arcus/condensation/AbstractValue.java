package io.sunshower.arcus.condensation;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public abstract class AbstractValue<T, E extends Enum<E>> implements Value<T, E> {

  final T value;
  final E type;

  public AbstractValue(final E type, final T value) {
    this.type = type;
    this.value = value;
  }

  @Override
  public E getType() {
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
