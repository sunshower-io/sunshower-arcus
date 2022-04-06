package com.aire.ux.condensation;

public interface PropertyScanner {

  /**
   * @param type the type to scan
   * @param traverseHierarchy traverse up the class hierarchy
   * @param includeInterfaces include scanning interfaces in the class hierarchy
   * @return the discovered properties
   */
  <T> TypeDescriptor<T> scan(Class<T> type, boolean traverseHierarchy, boolean includeInterfaces);

  /**
   * @param type the type to scan, excluding interfaces
   * @param traverseHierarchy traverse up the class hierarchy
   * @return the discovered properties
   */
  default <T> TypeDescriptor<T> scan(Class<T> type, boolean traverseHierarchy) {
    return scan(type, traverseHierarchy, false);
  }

  /**
   * @param type the type to scan, excluding interfaces but including linear supertypes
   * @return the discovered properties
   */
  default <T> TypeDescriptor<T> scan(Class<T> type) {
    return scan(type, true, false);
  }

  TypeInstantiator getTypeInstantiator();
}
