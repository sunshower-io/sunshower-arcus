package com.aire.ux.condensation;

import io.sunshower.lang.tuple.Pair;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import lombok.NonNull;
import lombok.val;

public interface Property<T extends AccessibleObject> {

  static boolean isPrimitive(Class<?> type) {
    return type.isPrimitive()
        || Boolean.class.equals(type)
        || Integer.class.equals(type)
        || Float.class.equals(type)
        || Double.class.equals(type)
        || Byte.class.equals(type)
        || Character.class.equals(type)
        || Short.class.equals(type);
  }

  static <T, U> boolean isConvertible(Class<T> from, Class<U> to) {
    return AbstractProperty.defaultConverters.containsKey(Pair.of(from, to));
  }

  static <T, U> U convert(T value, @NonNull Class<U> type) {
    if (value == null) {
      return null;
    }
    @SuppressWarnings("unchecked")
    val converter =
        (Function<T, U>) AbstractProperty.defaultConverters.get(Pair.of(value.getClass(), type));
    return converter.apply(value);
  }

  default boolean isPrimitive() {
    val type = getType();
    return isPrimitive(type);
  }

  boolean isConvertable();

  /**
   * @return a key converter if and only if isMap() returns true, a {@code @Convert} annotation is
   *     present on the property, and the {@code Converter(key=)} property is set to an instantiable
   *     type
   */
  Converter<?, ?> getKeyConverter();

  Converter<?, ?> getConverter();

  /**
   * if a property has a converter, apply that converter to convert the property to the desired type
   *
   * @param value
   * @param <T> the incoming type
   * @param <U> the result type
   * @return the converted value
   */
  <T, U> T convert(U value);

  /**
   * @param <U> the type that this property belongs to
   * @return the host of this property
   */
  <U> Class<U> getHost();

  T getMember();

  /**
   * @param <U> the type-parameter
   * @return the type of this property for instance, if this is a field such as {@code private
   *     String myName; } then this returns <code>java.lang.String</code>
   */
  <U> Class<U> getType();

  <T> Type getGenericType();

  default boolean isArray() {
    return getType().isArray();
  }

  default boolean isCollection() {
    return Collection.class.isAssignableFrom(getType());
  }

  @SuppressWarnings("unchecked")
  default <U> Class<U> getComponentType() {
    if (isArray()) {
      return (Class<U>) getType().getComponentType();
    }
    if (isCollection()) {
      val parameterizedType = (ParameterizedType) getGenericType();
      val typeArgs = parameterizedType.getActualTypeArguments();
      if (typeArgs != null && typeArgs.length > 0) {
        return (Class<U>) typeArgs[0];
      }
    }
    return getType();
  }

  /**
   * @return the physical name of the member if this is a field such as {@code private String
   *     myName; } then this returns <code>myName</code>
   */
  String getMemberReadName();

  String getMemberWriteName();

  /**
   * @return the <code>normalized</code> name of this member. For instance, if you have {@code class
   *     Sample { int myField; int getMyField() { <p>} <p>void setMyField(int myField) {
   *     this.myField = myField; } } } then the <code> memberReadName()</code> is <code>getMyField()
   * </code> and the <code>memberWriteName()</code> is <code>setMyField()</code> and the <code>
   * memberNormalizedName</code> is <code>myField
   * </code>.
   *     <p>Note that the memberNormalizedName is not required to correspond to any field on the
   *     host type
   */
  String getMemberNormalizedName();

  /**
   * the name of the document-element that this should be read from
   *
   * @return the read-alias of this property
   */
  String getReadAlias();

  /** @return the write-alias of this property */
  String getWriteAlias();

  /**
   * @param host the object to set this value on
   * @param value the value to set
   * @param <T> the type of the value
   * @param <U> the type of the host
   */
  <T, U> void set(U host, T value);

  /**
   * @param host the object to retrieve this value from
   * @param <T> the type of the value
   * @param <U> the type of the host
   * @return the value
   */
  <T, U> T get(U host);

  default boolean isMap() {
    return Map.class.isAssignableFrom(getType());
  }

  enum Mode {
    Read,
    Write,
    Normalized
  }
}
