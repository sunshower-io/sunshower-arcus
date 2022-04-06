package com.aire.ux.condensation.json;

import com.aire.ux.condensation.json.Value.Type;
import io.sunshower.arcus.ast.core.Token;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.val;

public class Values {

  public static final Value NULL_VALUE = new AbstractValue(Type.Null, null);

  public static Value string(Token value) {
    val lexeme = value.getLexeme();
    return new StringValue(lexeme.substring(1, lexeme.length() - 1));
  }

  public static ObjectValue object() {
    return new ObjectValue();
  }

  public static ArrayValue array() {
    return new ArrayValue();
  }

  static Value integer(String lexeme) {
    return new IntegerValue(Integer.parseInt(lexeme));
  }

  public static Value negate(Value value) {
    if (!(value instanceof IntegerValue)) {
      throw new IllegalArgumentException("Expected integral value");
    }
    return new IntegerValue(-((IntegerValue) value).getValue());
  }

  public static Value bool(String lexeme) {
    return new BooleanValue(Boolean.parseBoolean(lexeme));
  }

  public static Value nullValue() {
    return NULL_VALUE;
  }

  public static Value<Double> number(String lexeme) {
    return new NumericValue(lexeme);
  }

  static class NumericValue extends AbstractValue<Double> {

    public NumericValue(String lexeme) {
      super(Type.Number, new BigDecimal(lexeme).doubleValue());
    }
  }

  static class BooleanValue extends AbstractValue<Boolean> {

    public BooleanValue(boolean b) {
      super(Type.Boolean, b);
    }
  }

  static class IntegerValue extends AbstractValue<Integer> {

    public IntegerValue(final Integer value) {
      super(Type.Number, value);
    }
  }

  public static class ArrayValue extends AbstractValue<List<Value>> {

    public ArrayValue() {
      super(Type.Array, new ArrayList<>());
    }

    public void add(Value value) {
      getValue().add(value);
    }
  }

  public static class StringValue extends AbstractValue<String> {

    public StringValue(String value) {
      super(Type.String, value);
    }
  }

  public static class ObjectValue extends AbstractValue<Map<String, Value>> {

    public ObjectValue() {
      super(Type.Object, new LinkedHashMap<>());
    }

    public void set(String name, Value value) {
      getValue().put(name, value);
    }

    public Value get(String name) {
      return getValue().get(name);
    }

    public boolean has(String key) {
      return getValue().containsKey(key);
    }
  }
}
