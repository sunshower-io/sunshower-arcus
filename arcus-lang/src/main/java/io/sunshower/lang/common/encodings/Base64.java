package io.sunshower.lang.common.encodings;

import io.sunshower.lang.primitives.Rope;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class Base64 implements Encoding {

  @Override
  public boolean test(byte[] input) {
    return false;
  }

  @Override
  public boolean test(String input) {
    return false;
  }

  @Override
  public CharSequence encode(CharSequence sequence, Charset charset) {
    return null;
  }

  @Override
  public void encode(InputStream inputStream, OutputStream os, Charset charset) throws IOException {

  }

  @Override
  public void decode(InputStream inputStream, OutputStream outputStream, Charset charset)
      throws IOException {

  }

  @Override
  public String encode(byte[] input) {
    return null;
  }

  @Override
  public byte[] encode(byte[] input, Charset charset) {
    return new byte[0];
  }

  @Override
  public byte[] decode(String input) {
    return new byte[0];
  }

  @Override
  public byte[] decode(CharSequence input) {
    return new byte[0];
  }

  @Override
  public byte[] decode(char[] input) {
    return new byte[0];
  }

  @Override
  public byte[] decode(InputStream inputStream) throws IOException {
    return new byte[0];
  }

  @Override
  public String encode(String input) {
    return null;
  }

  @Override
  public Rope encode(Rope input, Charset charset) {
    return null;
  }

  @Override
  public Rope decode(Rope value, Charset charset) {
    return null;
  }
}
