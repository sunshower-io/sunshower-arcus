package com.aire.ux.condensation.mappings;

import com.aire.ux.condensation.Property;
import com.aire.ux.condensation.Property.Mode;
import com.aire.ux.condensation.TypeDescriptor;
import com.aire.ux.condensation.TypeInstantiator;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import lombok.NonNull;

@SuppressFBWarnings
public class ImmutableTypeDescriptor<T> implements TypeDescriptor<T> {

  private final Class<T> type;
  private final List<Property<?>> properties;
  private final TypeInstantiator instantiator;
  private final Map<Mode, Map<String, Property<?>>> propertyCache;

  public ImmutableTypeDescriptor(
      TypeInstantiator typeInstantiator, @NonNull Class<T> type, List<Property<?>> properties) {
    this.type = type;
    this.instantiator = typeInstantiator;
    this.properties = Collections.unmodifiableList(properties);
    this.propertyCache = new EnumMap<>(Mode.class);
  }

  @Override
  public Class<T> getType() {
    return type;
  }

  @Override
  public List<Property<?>> getProperties() {
    return properties;
  }

  @Override
  public Property<?> propertyNamed(Property.Mode mode, String name) {
    switch (mode) {
      case Read:
        return locate(mode, name, Property::getReadAlias);
      case Write:
        return locate(mode, name, Property::getWriteAlias);
      case Normalized:
        return locate(mode, name, Property::getMemberNormalizedName);
    }
    throw new IllegalStateException("shouldn't have reached here");
  }

  private Property<?> locate(
      Property.Mode mode, String name, Function<Property<?>, String> nameMapping) {
    return propertyCache
        .computeIfAbsent(mode, key -> new LRUCache<>(15))
        .computeIfAbsent(
            name,
            n ->
                properties.stream()
                    .filter(property -> n.equals(nameMapping.apply(property)))
                    .findAny()
                    .orElseThrow(
                        () ->
                            new NoSuchElementException(
                                String.format(
                                    "No readable property named '%s' on type '%s'", name, type))));
  }
}
