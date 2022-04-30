package io.sunshower.arcus.reflect;

import java.util.Optional;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public enum HierarchyTraversalMode {

  LinearSupertypes(LinearSuperTypeTraversalStrategy.class);


  final HierarchyTraversalStrategy strategy;
  HierarchyTraversalMode(Class<? extends HierarchyTraversalStrategy> strategy) {
    this.strategy = Reflect.instantiate(strategy);
  }


  Stream<Class<?>> apply(@Nullable Class<?> type)  {
    return Optional.ofNullable(type).stream().flatMap(strategy::apply);
  }

}
