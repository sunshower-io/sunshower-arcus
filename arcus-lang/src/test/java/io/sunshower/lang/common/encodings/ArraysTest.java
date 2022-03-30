package io.sunshower.lang.common.encodings;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import io.sunshower.lang.primitives.Arrays;
import lombok.val;
import org.junit.jupiter.api.Test;

class ArraysTest {


  @Test
  void ensureRemovedWorksForSmallArray() {
    val original = new char[]{'1', '2', '3', '4'};
    val removed = Arrays.remove(original, 1, 2);
    assertArrayEquals(removed, new char[]{'1', '4'});
  }

  @Test
  void ensureRemovingWorksForLargerArray() {
    val arr = new char[]{'1','2', '3', '4', '5', '6', '7'};
    val expected = new char[]{'1', '2', '6', '7'};
    assertArrayEquals(expected, Arrays.remove(arr, 2, 3));
  }

}