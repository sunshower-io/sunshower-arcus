package com.aire.ux.condensation.mappings;

import com.aire.ux.condensation.AbstractProperty;
import com.aire.ux.condensation.Convert;
import com.aire.ux.condensation.Converter;
import com.aire.ux.condensation.TypeInstantiator;
import io.sunshower.arcus.reflect.Reflect;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import lombok.NonNull;
import lombok.val;

@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class FieldProperty extends AbstractProperty<Field> {

  protected FieldProperty(
      TypeInstantiator instantiator,
      Field member,
      Class<?> host,
      String readAlias,
      String writeAlias) {
    super(instantiator, member, host, readAlias, writeAlias);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Class<U> getType() {
    return (Class<U>) getMember().getType();
  }

  @Override
  public <T> Type getGenericType() {
    return getMember().getGenericType();
  }

  @Override
  public String getMemberReadName() {
    return getMember().getName();
  }

  @Override
  public String getMemberWriteName() {
    return getMemberReadName();
  }

  @Override
  public String getMemberNormalizedName() {
    return getMemberReadName();
  }

  @Override
  public <T, U> void set(U host, T value) {
    try {
      getField(host).set(host, value);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T, U> T get(U host) {
    try {
      return (T) getField(host).get(host);
    } catch (IllegalAccessException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @NonNull
  private <U> Field getField(U host) {
    val field = getMember();
    if (!field.canAccess(host)) {
      if (!field.trySetAccessible()) {
        throw new IllegalStateException(
            String.format(
                "Error: field '%s' on type '%s' cannot be made accessible", field, getHost()));
      }
    }
    return field;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Converter<Field, ?> readConverter(Class<?> host, Field member) {
    if (member.isAnnotationPresent(Convert.class)) {
      val convert = member.getAnnotation(Convert.class);
      if (!Converter.class.equals(convert.value())) {
        return Reflect.instantiate(member.getAnnotation(Convert.class).value());
      }
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  protected Converter<String, ?> readKeyConverter(Class<?> host, Field member) {
    if (member.isAnnotationPresent(Convert.class)) {
      val type = member.getAnnotation(Convert.class);
      if (!Converter.class.equals(type.key())) {
        return getInstantiator().instantiate(type.key());
      }
    }
    return null;
  }
}
