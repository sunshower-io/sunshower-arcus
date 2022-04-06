package io.sunshower.arcus.condensation.mappings;

import io.sunshower.arcus.condensation.Property;
import java.util.Set;

/** a strategy for extracting properties from exactly one type (type, interface, etc.) */
public interface PropertyScanningStrategy {

  <T> Set<Property<?>> scan(Class<T> type);
}
