package com.aire.ux.condensation.mappings;

import com.aire.ux.condensation.AbstractProperty;
import com.aire.ux.condensation.Convert;
import com.aire.ux.condensation.Converter;
import com.aire.ux.condensation.TypeInstantiator;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.function.Function;
import lombok.NonNull;
import lombok.val;

public class MutatorProperty extends AbstractProperty<Mutator> {

  protected MutatorProperty(
      TypeInstantiator instantiator,
      Method getter,
      Method setter,
      Class<?> host,
      String readAlias,
      String writeAlias) {
    super(instantiator, new Mutator(getter, setter), host, readAlias, writeAlias);
    getMember().setAccessible(true);
    if (getMember().canAccess(this)) {
      throw new IllegalStateException(
          String.format(
              "Error: member " + "(read: %s, write: %s) on type %s is not accessible",
              getter, setter, getter.getDeclaringClass()));
    }
  }

  @Override
  public Converter<?, ?> getKeyConverter() {
    return null;
  }

  @Override
  public <U> Class<U> getType() {
    return getMember().getReturnType();
  }

  @Override
  public <T> Type getGenericType() {
    return getMember().getGenericType();
  }

  @Override
  public String getMemberReadName() {
    return getMember().getGetter().getName();
  }

  @Override
  public String getMemberWriteName() {
    return getMember().getSetter().getName();
  }

  @Override
  /** this expects that the member setter name */
  public String getMemberNormalizedName() {
    val name = getMember().getSetter().getName().substring(3);
    return Character.toLowerCase(name.charAt(0)) + name.substring(1);
  }

  @Override
  public <T, U> void set(U host, T value) {
    final Mutator member = getMutator(host);
    try {
      member.getSetter().invoke(host, value);
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T, U> T get(U host) {
    try {
      return (T) getMutator(host).getGetter().invoke(host);
    } catch (IllegalAccessException | InvocationTargetException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @NonNull
  private <U> Mutator getMutator(U host) {
    val member = getMember();
    if (!member.canAccess(host)) {
      if (!member.trySetAccessible()) {
        throw new IllegalStateException(
            String.format(
                "Error: mutator set (read:%s, write:%s) hosted by: %s is not accessible",
                member.getGetter(), member.getSetter(), getHost()));
      }
    }
    return member;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Converter<Mutator, ?> readConverter(Class<?> host, Mutator member) {
    if (member.isAnnotationPresent(Convert.class)) {
      val type = member.getAnnotation(Convert.class);
      if (!Converter.class.equals(type.value())) {
        return getInstantiator().instantiate(type.value());
      }
    }
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected Converter<String, ?> readKeyConverter(Class<?> host, Mutator member) {
    if (member.isAnnotationPresent(Convert.class)) {
      val type = member.getAnnotation(Convert.class);
      if (!Function.class.equals(type.key())) {
        return getInstantiator().instantiate(type.key());
      }
    }
    return null;
  }
}
