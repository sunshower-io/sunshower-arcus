package com.aire.ux.condensation.mappings;

import com.aire.ux.condensation.PropertyScanner;
import com.aire.ux.condensation.RootElement;
import com.aire.ux.condensation.TypeDescriptor;
import com.aire.ux.condensation.TypeInstantiator;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.val;

public class AnnotationDrivenPropertyScanner implements PropertyScanner {

  static final int DEFAULT_CACHE_SIZE = 100;

  private final TypeInstantiator typeInstantiator;
  private final Map<TypeDescriptorKey, TypeDescriptor<?>> typeDescriptorCache;

  public AnnotationDrivenPropertyScanner() {
    this(DEFAULT_CACHE_SIZE);
  }

  public AnnotationDrivenPropertyScanner(int cacheSize) {
    this(cacheSize, new ReflectiveTypeInstantiator());
  }

  public AnnotationDrivenPropertyScanner(TypeInstantiator instantiator) {
    this(DEFAULT_CACHE_SIZE, instantiator);
  }

  public AnnotationDrivenPropertyScanner(
      int cacheSize, @NonNull final TypeInstantiator instantiator) {
    this.typeInstantiator = instantiator;
    this.typeDescriptorCache = new LRUCache<>(DEFAULT_CACHE_SIZE);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> TypeDescriptor<T> scan(
      Class<T> type, boolean traverseHierarchy, boolean includeInterfaces) {
    val rootElement = type.getAnnotation(RootElement.class);
    if (rootElement == null) {
      throw new IllegalArgumentException(
          String.format(
              "Type '%s' must be annotated by @RootElement to be registered and scanned", type));
    }
    return (TypeDescriptor<T>)
        typeDescriptorCache.computeIfAbsent(
            new TypeDescriptorKey(type, traverseHierarchy, includeInterfaces),
            key ->
                new DelegatingPropertyScanner(
                        getTypeInstantiator(),
                        new FieldAnnotationPropertyScanningStrategy(typeInstantiator),
                        new MethodAnnotationPropertyScanningStrategy(typeInstantiator))
                    .scan(type, traverseHierarchy, includeInterfaces));
  }

  @Override
  public TypeInstantiator getTypeInstantiator() {
    return typeInstantiator;
  }

  @AllArgsConstructor
  @EqualsAndHashCode
  private static class TypeDescriptorKey {

    final Class<?> type;
    final boolean traverseHierarchy;
    final boolean includeInterfaces;
  }
}
