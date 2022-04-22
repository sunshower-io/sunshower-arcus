package io.sunshower.lang.common.encodings;

import io.sunshower.lang.primitives.Rope;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Locale;
import lombok.val;

public class Base64 implements Encoding {

  static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
  static final String LOWERCASE = UPPERCASE.toLowerCase(Locale.ROOT);
  static final String SPECIAL = "0123456789+/=";
  static final String ALPHABET = UPPERCASE + LOWERCASE + SPECIAL;
  final java.util.Base64.Encoder encoder;
  final java.util.Base64.Decoder decoder;

  Base64() {
    encoder = java.util.Base64.getEncoder();
    decoder = java.util.Base64.getDecoder();
  }

  @Override
  public boolean test(byte[] input) {
    for (int i = 0; i < input.length; i++) {
      val c = input[i];
      if (!inAlphabet((char) c)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean test(String input) {
    for (int i = 0; i < input.length(); i++) {
      int ch = input.charAt(i);
      if (!inAlphabet((char) ch)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public CharSequence encode(CharSequence sequence, Charset charset) {
    val bb = charset.encode(CharBuffer.wrap(sequence));
    return charset.decode(encoder.encode(bb));
  }

  @Override
  public void encode(InputStream inputStream, OutputStream os, Charset charset)
      throws IOException {

    byte[] data = new byte[256];
    int len;
    while ((len = inputStream.read(data)) != -1) {
      val bb = ByteBuffer.wrap(data, 0, len);
      os.write(encoder.encode(bb).array());
    }
  }

  @Override
  public void decode(InputStream inputStream, OutputStream outputStream, Charset charset)
      throws IOException {
    byte[] data = new byte[256];
    int len;
    while ((len = inputStream.read(data)) != -1) {
      var cb = charset.decode(ByteBuffer.wrap(data, 0, len));
      outputStream.write(charset.encode(cb).array());
    }
  }

  @Override
  public String encode(byte[] input) {
    return encoder.encodeToString(input);
  }

  @Override
  public byte[] encode(byte[] input, Charset charset) {
    val decoded = charset.decode(ByteBuffer.wrap(input));
    return charset.encode(decoded).array();
  }

  @Override
  public byte[] decode(String input) {
    return decoder.decode(input);
  }

  @Override
  public byte[] decode(CharSequence input, Charset charset) {
    val bb = charset.encode(CharBuffer.wrap(input));
    return decoder.decode(bb).array();
  }

  @Override
  public byte[] decode(char[] input, Charset charset) {
    val bb = charset.encode(CharBuffer.wrap(input));
    return decoder.decode(bb).array();
  }

  @Override
  public byte[] decode(InputStream inputStream) throws IOException {
    val output = new ByteArrayOutputStream();
    val chunk = new byte[256];
    int length;
    while ((length = inputStream.read(chunk)) != -1) {
      val bb = ByteBuffer.wrap(chunk, 0, length);
      val decoded = decoder.decode(bb);
      output.write(decoded.array(), 0, decoded.limit());
    }
    return output.toByteArray();
  }

  @Override
  public String encode(String input) {
    return encode((CharSequence) input).toString();
  }

  @Override
  public Rope encode(Rope input, Charset charset) {
    return new Rope(encode(input.sequentialCharacters(), charset));
  }

  @Override
  public Rope decode(Rope value, Charset charset) {
    return new Rope(decode(value.sequentialCharacters(), charset));
  }

  final boolean inAlphabet(char ch) {
    for (int j = 0; j < ALPHABET.length(); j++) {
      if (ch != ALPHABET.charAt(j)) {
        return false;
      }
    }
    return true;
  }
}
