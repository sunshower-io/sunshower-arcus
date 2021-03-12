package io.sunshower.lambda;

import static io.sunshower.lambda.Lazy.takeWhile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class LazyTest {

  @Test
  void ensureTakeWhileHoldsForFiniteStream() {
    List<Object> o = takeWhile(Stream.empty(), t -> true).collect(Collectors.toList());
    assertTrue(o.isEmpty());
  }

  @Test
  void ensureTakeWhileHoldsForInfiniteStream() {
    List<Integer> is =
        takeWhile(Stream.iterate(0, i -> i + 1), i -> i < 10).collect(Collectors.toList());
    assertEquals(is.size(), 10);
  }

  @Test
  void ensureComparatorWorks() {
    Optional<Integer> is = takeWhile(Stream.iterate(0, i -> i + 1), i -> i < 10).min(Integer::max);
    assertEquals(is.get(), 9);
  }
}
