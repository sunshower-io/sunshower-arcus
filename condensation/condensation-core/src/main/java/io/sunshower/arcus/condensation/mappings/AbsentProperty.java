package io.sunshower.arcus.condensation.mappings;

import io.sunshower.arcus.condensation.AbstractProperty;
import io.sunshower.arcus.condensation.Converter;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Type;

public class AbsentProperty<E extends AccessibleObject> extends AbstractProperty<E> {

  protected AbsentProperty() {
    super(null, null, null, null, null);
  }

  @Override
  protected Converter<E, ?> readConverter(Class<?> host, E member) {
    return null;
  }

  @Override
  protected Converter<String, ?> readKeyConverter(Class<?> host, E member) {
    return null;
  }

  @Override
  public <U> Class<U> getType() {
    return null;
  }

  @Override
  public <T> Type getGenericType() {
    return null;
  }

  @Override
  public String getMemberReadName() {
    return null;
  }

  @Override
  public String getMemberWriteName() {
    return null;
  }

  @Override
  public String getMemberNormalizedName() {
    return null;
  }

  @Override
  public <T, U> void set(U host, T value) {}

  @Override
  public <T, U> T get(U host) {
    return null;
  }

  @Override
  public boolean isArray() {
    return false;
  }

  @Override
  public boolean isCollection() {
    return false;
  }

  @Override
  public boolean isMap() {
    return false;
  }

  @Override
  public boolean isConvertable() {
    return false;
  }

  @Override
  public boolean isEnum() {
    return false;
  }

  @Override
  public boolean isPrimitive() {
    return false;
  }
}
