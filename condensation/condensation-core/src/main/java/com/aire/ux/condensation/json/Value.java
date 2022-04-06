package com.aire.ux.condensation.json;

import javax.annotation.Nonnull;

public interface Value<T> {

  enum Type {
    String,
    Object,
    Boolean,
    Hexadecimal,
    Array,
    Number,
    Null
  }

  @Nonnull
  Type getType();

  T getValue();

  default boolean isScalar() {
    switch (getType()) {
      case String:
      case Boolean:
      case Number:
      case Null:
      case Hexadecimal:
        return true;
      default:
        return false;
    }
  }
}
