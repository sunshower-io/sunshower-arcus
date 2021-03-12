package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class DoublesTest {

  @Test
  void ensureDoublesArrayIsCopiedCorrectly() {
    double[] d =
        new double[] {0.0d, 4.0d, 99.45d, 113414123451234.4d, Double.MAX_VALUE, Double.MIN_VALUE};

    double[] c = Doubles.fromByteArray(Doubles.toByteArray(d));
    assertArrayEquals(d, c, 0.0d);
  }
}
