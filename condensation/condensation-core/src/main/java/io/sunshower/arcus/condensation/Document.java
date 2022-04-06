package io.sunshower.arcus.condensation;

import java.util.Collection;
import java.util.function.Supplier;

public interface Document<E extends Enum<E>> {

  <U, T extends Value<U, E>> T getRoot();

  <U> U get(String key);

  <U, T extends Value<U, E>> T getValue(String key);

  <T> T select(String selector);

  Collection<?> selectAll(String selector);

  <T> T read(Class<T> type, TypeBinder<E> strategy);

  <U extends Collection<? super T>, T> U readAll(
      Class<T> type, Supplier<U> instantiator, TypeBinder<E> strategy);
}
