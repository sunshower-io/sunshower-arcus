package com.aire.ux.condensation.mappings;

import com.aire.ux.condensation.Property;
import java.util.Set;

/** a strategy for extracting properties from exactly one type (type, interface, etc.) */
public interface PropertyScanningStrategy {

  <T> Set<Property<?>> scan(Class<T> type);
}
