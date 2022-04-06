package io.sunshower.gyre;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

@SuppressFBWarnings
public final class ArrayIterator<T> implements Iterator<T> {

  private final T[] store;
  private int count;

  public ArrayIterator(T[] store) {
    Objects.requireNonNull(store);
    this.store = store;
  }

  @Override
  public boolean hasNext() {
    return count < store.length;
  }

  @Override
  public T next() {
    if (count < store.length) {
      return store[count++];
    }
    throw new NoSuchElementException();
  }
}
