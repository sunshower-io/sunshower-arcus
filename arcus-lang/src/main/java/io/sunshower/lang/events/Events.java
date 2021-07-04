package io.sunshower.lang.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

public class Events {

  public static <T> Event<T> create(T source) {
    return new DEvent<>(source);
  }
}

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
final class DEvent<T> implements Event<T> {

  final T target;
}
