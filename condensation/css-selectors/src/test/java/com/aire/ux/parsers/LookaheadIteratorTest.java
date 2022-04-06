package com.aire.ux.parsers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.ast.core.LookaheadIterator;
import java.util.List;
import lombok.val;
import org.junit.jupiter.api.Test;

class LookaheadIteratorTest {

  private LookaheadIterator<Integer> values;

  @Test
  void ensureUnreadingSingleValueWorks() {
    values = iteratorFor(1, 2, 3, 4);
    assertEquals(values.peek(), 1);
    assertEquals(values.peek(), 1);
    val next = values.next();
    assertEquals(1, next);

    assertEquals(2, values.peek());
    values.pushBack(next, 19);
    assertEquals(19, values.peek());
    values.next();
    assertEquals(values.peek(), 1);
  }

  private <T> LookaheadIterator<T> iteratorFor(T... values) {
    return LookaheadIterator.wrap(List.of(values).iterator());
  }
}
