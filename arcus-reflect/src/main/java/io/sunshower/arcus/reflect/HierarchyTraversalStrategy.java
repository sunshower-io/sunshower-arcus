package io.sunshower.arcus.reflect;

import java.util.stream.Stream;

public interface HierarchyTraversalStrategy {

  Stream<Class<?>> apply(Class<?> t);
}
