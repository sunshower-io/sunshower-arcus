package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.Test;

class RopeLikeOverCharacterArrayTest {

  @Test
  void ensureRopeCanBeConstructedFromSingleCharArray() {
    val rope = new RopeLikeOverCharacterArray("hello");
    assertEquals("hello", rope.toString());
  }

  @Test
  void ensureRopeCanBeConstructedFromManyCharArrays() {
    val rope = new RopeLikeOverCharacterArray("hello".toCharArray(), "world".toCharArray());
    assertEquals("helloworld", rope.toString());
  }

  @Test
  void ensureCharAtWorks() {
    val seg = "the quick brown fox jumped over the lazy dog";
    val str =
        Arrays.stream(seg.split("\\s+"))
            .map(String::toCharArray)
            .collect(Collectors.toList())
            .toArray(new char[0][0]);
    val segs = String.join("", seg.split("\\s+"));
    val rope = new RopeLikeOverCharacterArray(str);
    assertEquals(segs.length(), rope.length());
    for (int i = 0; i < segs.length(); i++) {
      assertEquals(segs.charAt(i), rope.charAt(i));
    }
  }
}
