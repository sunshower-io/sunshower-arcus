package io.sunshower.lambda;

@FunctionalInterface
public interface Exceptional<T, U, V extends Throwable> {
  U apply(T t) throws V;
}
