package io.sunshower.arcus.reflect;

import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.val;

public class LinearSuperTypeTraversalStrategyExcludingObject implements HierarchyTraversalStrategy {

  public LinearSuperTypeTraversalStrategyExcludingObject() {}

  @Override
  public Stream<Class<?>> apply(Class<?> t) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(new LinearSupertypeIterator(t), Spliterator.ORDERED),
        false);
  }

  private static final class LinearSupertypeIterator implements Iterator<Class<?>> {

    Class<?> type;

    LinearSupertypeIterator(Class<?> type) {
      this.type = type;
    }

    @Override
    public boolean hasNext() {
      return !Objects.equals(type, Object.class);
    }

    @Override
    public Class<?> next() {
      val result = type;
      type = type.getSuperclass();
      return result;
    }
  }
}
