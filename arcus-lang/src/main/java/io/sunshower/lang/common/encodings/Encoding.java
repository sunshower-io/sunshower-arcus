package io.sunshower.lang.common.encodings;

import io.sunshower.lang.primitives.Rope;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/** Created by haswell on 7/17/17. */
public interface Encoding {

  boolean test(byte[] input);

  boolean test(String input);

  CharSequence encode(CharSequence sequence, Charset charset);

  default CharSequence encode(CharSequence sequence) {
    return encode(sequence, Charset.defaultCharset());
  }

  /**
   * encode the inputstream to the outputstream
   *
   * @param inputStream the inputstream to encode
   * @param os the outputstream to write the encoded stream to
   * @param charset the character set to use
   * @throws IOException if an exception is encountered
   */
  void encode(InputStream inputStream, OutputStream os, Charset charset) throws IOException;

  /**
   * encode the inputstream to the outputstream
   *
   * @param inputStream the inputstream to encode
   * @param outputStream the outputstream to write the encoded stream to
   * @throws IOException if an exception is encountered
   */
  default void encode(InputStream inputStream, OutputStream outputStream) throws IOException {
    encode(inputStream, outputStream, Charset.defaultCharset());
  }

  /**
   * decode the inputstream to the outputstream using the provided charset
   *
   * @param inputStream the inputstream to decode from
   * @param outputStream the outputstream to decode to
   * @param charset the character set to use
   * @throws IOException if an exception is encountered
   */
  void decode(InputStream inputStream, OutputStream outputStream, Charset charset)
      throws IOException;

  /**
   * decode the inputstream to the outputstream
   *
   * @param inputStream the inputstream to decode from
   * @param outputStream the outputstream to decode to
   * @throws IOException if an exception is encountered
   */
  default void decode(InputStream inputStream, OutputStream outputStream) throws IOException {
    decode(inputStream, outputStream, Charset.defaultCharset());
  }

  /**
   * encode the provided bytes to a string
   *
   * @param input the input bytes
   * @return a string containing the encoded data
   */
  String encode(byte[] input);

  /**
   * encode the byte array to the resulting byte array over the provided character set
   *
   * @param input the input to encode
   * @param charset the character set to use
   * @return
   */
  byte[] encode(byte[] input, Charset charset);

  /**
   * decode the input to the byte array
   *
   * @param input the input to encode
   * @return the result decoded
   */
  byte[] decode(String input);

  /**
   * decode the input to the byte array
   *
   * @param input the input to decode
   * @return a byte array containing the decoded data
   */
  byte[] decode(CharSequence input, Charset charset);

  default byte[] decode(CharSequence input) {
    return decode(input, Charset.defaultCharset());
  }

  /**
   * decode the input to the byte array
   *
   * @param input the input to decode
   * @return a byte array containing the decoded data
   */
  byte[] decode(char[] input, Charset charset);

  default byte[] decode(char[] input) {
    return decode(input, Charset.defaultCharset());
  }

  /**
   * decode the input to the byte array
   *
   * @param inputStream the input to decode
   * @return a byte array containing the decoded data
   */
  byte[] decode(InputStream inputStream) throws IOException;

  /**
   * encode the input to the byte array
   *
   * @param input the input to encode
   * @return the result
   */
  String encode(String input);

  /**
   * @param input
   * @return
   */
  Rope encode(Rope input, Charset charset);

  default Rope encode(Rope input) {
    return encode(input, Charset.defaultCharset());
  }

  Rope decode(Rope value, Charset charset);

  default Rope decode(Rope value) {
    return decode(value, Charset.defaultCharset());
  }
}
