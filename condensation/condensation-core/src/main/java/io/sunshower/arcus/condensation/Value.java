package io.sunshower.arcus.condensation;

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
  E getType();

  T getValue();

  boolean isScalar();
}
