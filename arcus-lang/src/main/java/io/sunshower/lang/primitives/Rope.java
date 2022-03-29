package io.sunshower.lang.primitives;

import java.nio.charset.Charset;
import java.util.Arrays;
import lombok.NonNull;

public final class Rope implements CharSequence {

  final RopeLike base;

  protected Rope(@NonNull RopeLike base) {
    this.base = base;
  }

  public Rope() {
    this.base = Ropes.EMPTY;
  }

  public Rope(byte[] bytes) {
    this(bytes, Charset.defaultCharset());
  }

  public Rope(byte[] bytes, Charset charset) {
    int chunksize = Ropes.combinedLength;
    RopeLike root = null;
    for (int i = 0; i < bytes.length; i += chunksize) {
      byte[] bs = Arrays.copyOfRange(bytes, i, Math.min(i + chunksize, bytes.length));
      if (root == null) {
        root = new RopeLikeOverCharacterArray(Bytes.getCharacters(bs, charset));
      } else {
        root = root.append(new RopeLikeOverCharacterArray(Bytes.getCharacters(bs, charset)));
      }
    }
    base = root;
  }

  public Rope(String s) {
    this(s.getBytes(), Charset.defaultCharset());
  }

  public Rope(String s, Charset charset) {
    this(s.getBytes(), charset);
  }

  @Override
  public int length() {
    return base.length();
  }

  @Override
  public char charAt(int i) {
    return base.charAt(i);
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    return base.subSequence(start, end);
  }

  @Override
  public boolean equals(Object o) {
    return base.equals(o);
  }

  @Override
  public int hashCode() {
    return base.hashCode();
  }

  @Override
  public String toString() {
    return base.toString();
  }
}
