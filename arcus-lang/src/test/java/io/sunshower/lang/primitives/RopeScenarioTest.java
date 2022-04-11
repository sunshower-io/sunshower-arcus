package io.sunshower.lang.primitives;

import static io.sunshower.lang.primitives.RopesTest.print;
import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.WINDOWS)
class RopeScenarioTest {

  private Rope rope;
  private RopeLikeTree branch1;
  private RopeLikeTree branch2;
  private RopeLikeTree branch3;

  static String value = "Hello my name is Josiah";

  @BeforeEach
  void setUp() {

    branch1 =
        new RopeLikeTree(
            new RopeLikeOverCharSequence("Hello"), new RopeLikeOverCharacterArray(" my"));

    branch2 =
        new RopeLikeTree(new RopeLikeOverCharSequence(" na"), new RopeLikeOverCharSequence("me i"));

    branch3 =
        new RopeLikeTree(new RopeLikeOverCharSequence("s"), new RopeLikeOverCharSequence(" Josiah"));

    val branch1_r = new RopeLikeTree(branch2, branch3);
    assertEquals(7, branch1_r.weight());

    val branch1_l = new RopeLikeTree(branch1, branch1_r);

    rope = new Rope(branch1_l);
    assertEquals(23, rope.length());
  }

  @Test
  void ensureLengthIsExpected() {
    assertEquals(23, rope.length());
  }

  @Test
  void ensureDeleteWorks() {
    val r = rope.base.delete(0, 1);
    assertEquals(rope.toString().substring(1), r.toString());
  }

  @Test
  void ensureRopeSubstringWorks() {
    val rope = new Rope("Hello");
    val r = rope.substring(1, 3);
    assertEquals(r.toString(), "Hello".substring(1, 3));
  }

  @Test
  void ensureRopeSubstringWorksLongerString() {
    val expected = value.substring(10, 15);
    val r = rope.substring(10, 15);
    assertEquals(r.toString(), expected);
  }

  @Test
  void ensureIterationWorksAsExpected() {
    val r = rope.base.iterator();
    while(r.hasNext()) {
      val b = r.next();
      System.out.println(b);
    }
  }

  @Test
  void ensureSplittingBaseWorks() {
    val ropes = rope.base.split(11);

    print(rope);
    print(new Rope(ropes.fst));
    print(new Rope(ropes.snd));
  }
}
