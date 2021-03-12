package io.sunshower.lang.common.encodings;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.BitSet;

/** Created by haswell on 7/17/17. */
public class Base58 implements Encoding {

  private final char zero;
  private final char[] alphabet;

  public enum Alphabets {
    Default("123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz"),
    Ripple("rpshnaf39wBUDNEGHJKLM4PQRST7VWXYZ2bcdeCg65jkm8oFqi1tuvAxyz"),
    Flickr("123456789abcdefghijkmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ");

    final char[] data;

    Alphabets(String input) {
      this.data = input.toCharArray();
    }

    public char[] getAlphabet() {
      return Arrays.copyOf(data, data.length);
    }
  }

  final int[] indexes;

  private Base58(Alphabets alphabet) {
    this(alphabet.data);
  }

  private Base58(char[] alphabet) {
    this.alphabet = alphabet;
    this.zero = alphabet[0];
    indexes = new int[128];
    Arrays.fill(indexes, -1);
    for (int i = 0; i < alphabet.length; i++) {
      indexes[alphabet[i]] = i;
    }
  }

  @Override
  public boolean test(byte[] input) {
    final BitSet fst = new BitSet();
    for (char b : alphabet) {
      fst.set(b);
    }

    for (byte b : input) {
      if (!fst.get(b)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean test(String input) {

    final BitSet fst = new BitSet();
    for (char b : alphabet) {
      fst.set(b);
    }

    for (int i = 0; i < input.length(); i++) {
      if (!fst.get(input.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void encode(byte[] input, OutputStream os) throws IOException {
    int length = input.length;
    int zeros = 0, i, j, carry;
    while (zeros < length && input[zeros] == 0) {
      zeros++;
    }

    int size = (length - zeros) * 138 / 100 + 1;

    byte[] result = new byte[size];

    int max = size - 1;

    for (i = zeros; i < length; i++) {
      j = size - 1;
      for (carry = input[i]; j > max || carry != 0; j--) {
        carry = carry + 256 * input[j];
        result[j] = (byte) (carry % 58);
        carry /= 58;
      }
      max = j;
    }

    for (j = 0; j < size && result[j] == 0; j++)
      ;

    if (zeros != 0) {
      for (i = 0; i < zeros; i++) {
        os.write(zero);
      }
    }

    for (i = zeros; j < size; i++) {
      os.write(alphabet[result[j++]]);
    }
  }

  @Override
  @SuppressWarnings("PMD.AssignmentInOperand")
  public String encode(byte[] in) {

    byte[] input = Arrays.copyOf(in, in.length);

    int length = input.length;

    if (length == 0) {
      return "";
    }

    int zeros = 0;
    while (zeros < length && input[zeros] == 0) {
      zeros++;
    }

    final char[] encoded = new char[input.length * 2];

    int output = encoded.length;

    for (int start = zeros; start < length; ) {
      encoded[--output] = alphabet[mod(input, start, 256, 58)];
      if (input[start] == 0) {
        ++start;
      }
    }

    while (output < encoded.length && encoded[output] == 0) {
      ++output;
    }

    while (--zeros >= 0) {
      encoded[--output] = zero;
    }

    return new String(encoded, output, encoded.length - output);
  }

  @Override
  public byte[] decode(String input) {
    int length = input.length();
    if (length == 0) {
      return new byte[0];
    }

    byte[] bytes = new byte[length];

    for (int i = 0; i < length; i++) {
      char ch = input.charAt(i);
      int digit = ch < 128 ? indexes[ch] : -1;

      if (digit < 0) {
        throw new IllegalArgumentException(
            String.format("Unexpected character '%c' at index %d", ch, i));
      }

      bytes[i] = (byte) digit;
    }

    int zeros = 0;
    while (zeros < length && bytes[zeros] == 0) {
      ++zeros;
    }

    byte[] decoded = new byte[length];

    int start = length;

    for (int istart = zeros; istart < length; ) {
      decoded[--start] = mod(bytes, istart, 58, 256);
      if (bytes[istart] == 0) {
        ++istart;
      }
    }

    while (start < length && decoded[start] == 0) {
      ++start;
    }

    return Arrays.copyOfRange(decoded, start - zeros, length);
  }

  @Override
  public String encode(String input) {
    return encode(input.getBytes(Charset.defaultCharset()));
  }

  static byte mod(byte[] d, int fst, int base, int divisor) {
    int rem = 0;
    for (int i = fst; i < d.length; i++) {
      int digit = (int) d[i] & 0xFF;
      int t = rem * base + digit;
      d[i] = (byte) (t / divisor);
      rem = t % divisor;
    }
    return (byte) rem;
  }

  @SuppressWarnings("PMD.SingletonClassReturningNewInstance")
  public static Encoding getInstance(Alphabets alphabet) {
    return new Base58(alphabet);
  }
}
