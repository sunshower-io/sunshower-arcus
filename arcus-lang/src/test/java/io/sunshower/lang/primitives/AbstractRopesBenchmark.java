package io.sunshower.lang.primitives;

import java.nio.charset.StandardCharsets;
import java.util.Random;
import lombok.val;

public class AbstractRopesBenchmark {

  static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
  static final String NUMBERS = "0123456789";
  static final String UPPER_CASE = LOWER_CASE.toUpperCase();
  static final String ALPHABET = LOWER_CASE + NUMBERS + UPPER_CASE;

  protected static byte[] generateCharactersOfLength(int len) {
    val random = new Random();
    val result = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      result.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
    }
    return result.toString().getBytes(StandardCharsets.UTF_8);
  }

  protected static byte[] oneKb() {
    return generateCharactersOfLength(1000);
  }

  protected static byte[] tenKb() {
    return generateCharactersOfLength(10 * 1000);
  }

  protected static byte[] hundredKb() {
    return generateCharactersOfLength(10 * 10 * 1000);
  }

  protected static byte[] oneMb() {
    return generateCharactersOfLength(10 * 10 * 10 * 1000);
  }

  protected static byte[] tenMb() {
    return generateCharactersOfLength(10 * 10 * 10 * 10 * 1000);
  }

  protected static byte[] hundredMb() {
    return generateCharactersOfLength(10 * 10 * 10 * 10 * 10 * 1000);
  }
}
