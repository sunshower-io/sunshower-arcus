package io.sunshower.arcus.condensation;

import javax.annotation.Nonnull;

public interface Value<T, E extends Enum<E>> {

//  enum Type {
//    String,
//    Object,
//    Boolean,
//    Hexadecimal,
//    Array,
//    Number,
//    Null
//  }
//
  @Nonnull
  E getType();

  T getValue();

  boolean isScalar();
}
