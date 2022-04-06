package io.sunshower.persistence.id;

import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class IDSequenceTest {

  private Sequence<Identifier> sequence;

  @BeforeEach
  void setUp() {
    sequence = Identifiers.newSequence(true);
  }

  @Test
  void ensureSequenceGeneratesSequentialValues() {
    val fst = sequence.next();
    val snd = sequence.next();
    assertEquals(fst.compareTo(snd), -1);
  }

  @Test
  void ensureNumericRepresentationIsCorrectlyOrdered() {
    val fst = Identifiers.toNumeric(sequence.next());
    val snd = Identifiers.toNumeric(sequence.next());
    assertEquals(fst.compareTo(snd), -1);
  }
}
