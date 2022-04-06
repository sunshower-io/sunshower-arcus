package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;

@DisabledOnOs(OS.WINDOWS)
public class RopeTest {

  @Test
  void ensureRopeLastFibIsCorrect() {
    val results = Longs.computeFibonacciUntil(7540113804746346429L);
    assertEquals(4660046610375530309L, results[results.length - 2]);
  }
}
