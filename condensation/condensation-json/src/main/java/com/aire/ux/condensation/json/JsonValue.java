package com.aire.ux.condensation.json;

import com.aire.ux.condensation.json.JsonValue.Type;
import io.sunshower.arcus.condensation.Value;
import javax.annotation.Nonnull;

public class JsonValue<T> implements Value<T, Type> {

  public enum Type {
    String,
    Object,
    Boolean,
    Hexadecimal,
    Array,
    Number,
    Null
  }

  final T value;
  final Type type;

  public JsonValue(final Type type, final T value) {
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

  public boolean isScalar() {
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
