package io.sunshower.lang;

import java.util.function.Supplier;

public class Suppliers {

  public static <T> Supplier<T> memoize(Supplier<T> delegate) {
    return new MemoizingSupplier<>(delegate);
  }

  static final class MemoizingSupplier<T> implements Supplier<T> {

    private volatile boolean set;
    private final Supplier<T> delegate;
    private T value;

    MemoizingSupplier(final Supplier<T> delegate) {
      this.delegate = delegate;
    }


    @Override
    public T get() {
      if (!set) {
        synchronized (this) {
          if (!set) {
            var value = this.value;
            if (value == null) {
              set = true;
              this.value = value = delegate.get();
            }
          }
        }
      }
      return value;
    }
  }

}
