package io.sunshower.lang.common.encodings;

import io.sunshower.lang.primitives.Rope;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.BitSet;
import lombok.val;

/**
 * Created by haswell on 7/17/17.
 */
public class Base58 implements Encoding {

  final int[] indexes;
  private final char zero;
  private final char[] alphabet;

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


  static byte mod(ByteBuffer d, int fst, int base, int divisor) {
    int rem = 0;
    val length = d.limit();
    for (int i = fst; i < length; i++) {
      int digit = (int) d.get(i) & 0xFF;
      int t = rem * base + digit;
      d.put(i, (byte) (t / divisor));
      rem = t % divisor;
    }
    return (byte) rem;
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

  public static Encoding getInstance() {
    return getInstance(Alphabets.Default);
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
  public CharSequence encode(CharSequence sequence, Charset charset) {
    val enc = charset.encode(CharBuffer.wrap(sequence));
    val result = doEncode(enc, enc.limit());
    val encoded = result.encoded;
    val output = result.output;
    return CharBuffer.wrap(result.encoded, output, encoded.length - output);
  }


  @Override
  public void encode(InputStream in, OutputStream os, Charset charset) throws IOException {
    val bytes = in.readAllBytes();
    val result = doEncode(bytes);
    val encoded = result.encoded;
    val output = result.output;
    val charseq = new String(result.encoded, output, encoded.length - output);
    os.write(charseq.getBytes(charset));
  }

  @Override
  public void decode(InputStream inputStream, OutputStream outputStream, Charset charset)
      throws IOException {
    byte[] bytes = inputStream.readAllBytes();
    int length = bytes.length;
    val bb = ByteBuffer.wrap(bytes, 0, length);
    val result = doDecode(charset.decode(bb), length);
    outputStream.write(result.decoded, result.from, result.length - result.from);
  }


  @Override
  @SuppressWarnings("PMD.AssignmentInOperand")
  public String encode(byte[] in) {
    val result = doEncode(in);
    val encoded = result.encoded;
    val output = result.output;
    return new String(encoded, output, encoded.length - output);
  }

  @Override
  public byte[] encode(byte[] in, Charset charset) {
    val encoded = doEncode(in);
    val charbuffer = CharBuffer.wrap(encoded.encoded, 0, encoded.encoded.length - encoded.output);
    return charset.encode(charbuffer).array();
  }


  @Override
  public byte[] decode(String input) {
    return decode((CharSequence) input);
  }


  @Override
  public byte[] decode(CharSequence input) {
    val byteResult = doDecode(input, input.length());
    return Arrays.copyOfRange(byteResult.decoded, byteResult.from, byteResult.length);
  }

  @Override
  public byte[] decode(char[] input) {
    return decode(new CharArraySlice(input));
  }

  @Override
  public byte[] decode(InputStream inputStream) throws IOException {
    val outputStream = new ByteArrayOutputStream();
    decode(inputStream, outputStream);
    return outputStream.toByteArray();
  }

  @Override
  public String encode(String input) {
    return encode(input.getBytes(Charset.defaultCharset()));
  }

  @Override
  public Rope encode(Rope input, Charset charset) {
    return new Rope(encode(input.sequentialCharacters(), charset));
  }

  @Override
  public Rope decode(Rope value, Charset charset) {
    val result = doDecode(value.sequentialCharacters(), value.length());
    return new Rope(result.decoded, result.from, result.length - result.from, charset);
  }


  private Result doEncode(byte[] in) {
    return doEncode(in, in.length);
  }


  private ByteResult doDecode(CharSequence input, int length) {
    if (length == 0) {
      return new ByteResult(0, 0, new byte[0]);
    }
    val bytes = new byte[length];
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
    val decoded = new byte[length];
    int start = length;
    for (int c = zeros; c < length; ) {
      decoded[--start] = mod(bytes, c, 58, 256);
      if (bytes[c] == 0) {
        ++c;
      }
    }
    while (start < length && decoded[start] == 0) {
      ++start;
    }
    return new ByteResult(start - zeros, length, decoded);
  }


  private Result doEncode(ByteBuffer in, int length) {

    val input = in.duplicate();
    if (length == 0) {
      return new Result(0, new char[0]);
    }

    int zeros = 0;
    while (zeros < length && input.get(zeros) == 0) {
      zeros++;
    }
    val encoded = new char[length * 2];
    int output = encoded.length;
    for (int start = zeros; start < length; ) {
      encoded[--output] = alphabet[mod(input, start, 256, 58)];
      if (input.get(start) == 0) {
        ++start;
      }
    }
    while (output < encoded.length && encoded[output] == 0) {
      ++output;
    }

    while (--zeros >= 0) {
      encoded[--output] = zero;
    }
    return new Result(output, encoded);
  }


  private Result doEncode(byte[] in, int length) {
    val input = Arrays.copyOf(in, length);
    if (length == 0) {
      return new Result(0, new char[0]);
    }

    int zeros = 0;
    while (zeros < length && input[zeros] == 0) {
      zeros++;
    }
    val encoded = new char[length * 2];
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
    return new Result(output, encoded);
  }

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

  static final class ByteResult {

    final byte[] decoded;
    int from;
    int length;

    public ByteResult(int from, int length, byte[] decoded) {
      this.from = from;
      this.length = length;
      this.decoded = decoded;
    }
  }

  static final class Result {

    final int output;
    final char[] encoded;

    Result(int output, char[] encoded) {
      this.output = output;
      this.encoded = encoded;
    }
  }
}
