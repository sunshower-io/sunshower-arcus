package io.sunshower.arcus.condensation.json;

import io.sunshower.arcus.condensation.Value;
import io.sunshower.arcus.condensation.json.JsonValue.Type;
import javax.annotation.Nonnull;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
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
