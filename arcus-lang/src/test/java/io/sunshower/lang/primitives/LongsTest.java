package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;
import lombok.val;
import org.junit.jupiter.api.Test;

public class LongsTest {

  @Test
  void testFibonacci() {
    val c = Longs.computeFibonacciUntil(17);
    System.out.println(Arrays.toString(c));

    val s = Longs.computeFibonacciUntil(7540113804746346429L);
    System.out.println(Arrays.toString(s));

    val s2 = Longs.compute2(7540113804746346429L);
    System.out.println(s2);
  }

  @Test
  public void ensureCopyingLongsProducesExpectedResult() {
    long[] l = new long[] {0, -123124, Long.MAX_VALUE, Long.MIN_VALUE};
    long[] k = Longs.fromByteArray(Longs.toByteArray(l));
    assertArrayEquals(l, k);
  }
}
