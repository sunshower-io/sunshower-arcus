package io.sunshower.lang;

import java.util.function.Supplier;

public class Suppliers {

  public static <T> Supplier<T> memoize(Supplier<T> delegate) {
    return new MemoizingSupplier<>(delegate);
  }

  static final class MemoizingSupplier<T> implements Supplier<T> {

    private final Supplier<T> delegate;
    private volatile T value;

    MemoizingSupplier(final Supplier<T> delegate) {
      this.delegate = delegate;
    }

    @Override
    public T get() {
      var value = this.value;
      if (value == null) {
        synchronized (this) {
          value = this.value;
          if (value == null) {
            this.value = value = delegate.get();
          }
        }
      }
      return value;
    }
  }
}
