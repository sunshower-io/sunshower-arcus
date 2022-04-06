package com.aire.ux.condensation;

import java.util.List;

public interface TypeDescriptor<T> {

  Class<T> getType();

  List<Property<?>> getProperties();

  default Property<?> getProperty(int i) {
    return getProperties().get(i);
  }

  Property<?> propertyNamed(Property.Mode mode, String name);
}
