package io.sunshower.arcus.lang.benchmarks;


import io.sunshower.lang.primitives.Rope;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Group)
public class AbstractRopeBenchmark {

  static final String LOWER_CASE = "abcdefghijklmnopqrstuvwxyz";
  static final String NUMBERS = "0123456789";
  static final String UPPER_CASE = LOWER_CASE.toUpperCase();
  static final String ALPHABET = LOWER_CASE + NUMBERS + UPPER_CASE;



  protected Rope rope;
  protected String string;



  protected static byte[] generateCharactersOfLength(int len) {
    var random = new Random();
    var result = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      result.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
    }
    return result.toString().getBytes(StandardCharsets.UTF_8);
  }

  public static ByteStringBuilder byteString() {
    return new ByteStringBuilder();
  }

  public enum Bytes {

    BYTE {
      int multiplier() {
        return 1;
      }
    },

    DEKABYTE {
      int multiplier() {
        return 10;
      }
    },

    HECTOBYTE {
      int multiplier() {
        return 100;
      }
    },

    KILOBYTE {
      int multiplier() {
        return 100 * 10;
      }
    },

    MEGABYTE {
      int multiplier() {
        return 1000 * 1000;
      }
    };

    abstract int multiplier();
  }

  public static class ByteStringBuilder {

    public static byte[] ofLength(int length, Bytes unit) {
      return generateCharactersOfLength(length * unit.multiplier());
    }

  }

}
