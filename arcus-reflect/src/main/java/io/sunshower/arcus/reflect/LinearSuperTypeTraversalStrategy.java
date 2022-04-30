package io.sunshower.arcus.reflect;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class LinearSuperTypeTraversalStrategy implements
    HierarchyTraversalStrategy {


  public LinearSuperTypeTraversalStrategy() {

  }

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
      return type.getSuperclass() != null;
    }

    @Override
    public Class<?> next() {
      return (type = type.getSuperclass());
    }
  }
}
