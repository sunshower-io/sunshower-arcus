package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class IntegersTest {

  int[] copy(int[] a) {
    return Integers.fromByteArray(Integers.toByteArray(a));
  }

  @Test
  public void ensureIntegerArrayIsCopiedCorrectly() {
    int[] a = new int[] {1, 2, 3, 4, 5, 6, 7, 18, Integer.MAX_VALUE, Integer.MIN_VALUE};
    assertArrayEquals(Integers.fromByteArray(Integers.toByteArray(a)), a);
  }
}
