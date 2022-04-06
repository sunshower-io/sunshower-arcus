package com.aire.ux.condensation;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.lang.tuple.Pair;
import java.lang.reflect.AccessibleObject;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.val;

@SuppressFBWarnings
@SuppressWarnings({"unchecked", "PMD.AvoidDuplicateLiterals", "PMD.CompareObjectsWithEquals"})
public abstract class AbstractProperty<T extends AccessibleObject> implements Property<T> {

  static final Map<Pair<Class<?>, Class<?>>, Function<?, ?>> defaultConverters;

  static {
    defaultConverters = new HashMap<>();

    val loader =
        ServiceLoader.load(ConverterProvider.class, Thread.currentThread().getContextClassLoader())
            .iterator();
    while (loader.hasNext()) {
      val next = loader.next();
      defaultConverters.put(next.getTypeMapping(), next.getConverter());
    }
  }

  private final T member;
  /** the type of the host-class */
  private final Class<?> host;
  /** the read-alias of this property */
  private final String readAlias;

  private final String writeAlias;
  private final Converter<T, ?> converter;
  private final TypeInstantiator instantiator;
  private final Converter<String, ?> keyConverter;

  protected AbstractProperty(
      final TypeInstantiator instantiator,
      final T member,
      final Class<?> host,
      final String readAlias,
      final String writeAlias) {
    this.host = host;
    this.member = member;
    this.readAlias = readAlias;
    this.writeAlias = writeAlias;
    this.instantiator = instantiator;
    this.converter = readConverter(host, member);
    this.keyConverter = readKeyConverter(host, member);
  }

  protected AbstractProperty(
      @NonNull TypeInstantiator instantiator,
      @NonNull final T member,
      @NonNull final Class<?> host,
      @NonNull final String readAlias,
      @NonNull final String writeAlias,
      @Nullable final Converter<T, ?> converter,
      @Nullable final Converter<String, ?> keyConverter) {
    this.host = host;
    this.member = member;
    this.readAlias = readAlias;
    this.writeAlias = writeAlias;
    this.converter = converter;
    this.keyConverter = keyConverter;
    this.instantiator = instantiator;
  }

  protected TypeInstantiator getInstantiator() {
    return instantiator;
  }

  protected abstract Converter<T, ?> readConverter(Class<?> host, T member);

  protected abstract Converter<String, ?> readKeyConverter(Class<?> host, T member);

  @Override
  public Converter<?, ?> getKeyConverter() {
    return keyConverter;
  }

  @Override
  public Converter<?, ?> getConverter() {
    return converter;
  }

  @Override
  public boolean isConvertable() {
    return converter != null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U> Class<U> getHost() {
    return (Class<U>) host;
  }

  @Override
  public T getMember() {
    return member;
  }

  @Override
  @SuppressWarnings({"unchecked", "rawtypes"})
  public <R, S> R convert(S value) {
    if (converter == null) {
      if (value == null) {
        // map null to null
        return null;
      }
      val type = isArray() || isCollection() ? getComponentType() : getType();
      val converter = (Function<S, R>) defaultConverters.get(Pair.of(value.getClass(), type));
      if (converter != null) {
        return converter.apply(value);
      }
    } else {
      return ((Converter<R, S>) converter).read(value);
    }
    if (getType().isEnum()) {
      return (R) Enum.valueOf((Class) getType(), (String) value);
    }
    return (R) value;
  }

  @Override
  public String getReadAlias() {
    return readAlias;
  }

  @Override
  public String getWriteAlias() {
    return writeAlias;
  }
}
