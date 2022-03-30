package io.sunshower.lang.primitives;

import java.nio.charset.Charset;
import java.util.Arrays;
import lombok.NonNull;

/**
 * implementation of the Rope data-structure
 */
public final class Rope implements CharSequence {

  /**
   * the base of this rope
   */
  @NonNull
  final RopeLike base;

  /**
   * construct a rope over a RopeLike--for internal use only
   *
   * @param base the base to construct this over
   */
  protected Rope(@NonNull RopeLike base) {
    this.base = base;
  }


  public Rope() {
    this(Ropes.EMPTY);
  }

  /**
   * Construct a rope over the backing bytes.  If the byte array is larger than the segment-length,
   * a deep rope will be created
   *
   * @param bytes the bytes to construct this over
   */
  public Rope(@NonNull byte[] bytes) {
    this(bytes, Charset.defaultCharset());
  }


  /**
   * Construct the rope with the backing bytes
   *
   * @param bytes   the bytes
   * @param charset the charset to use--only used for transforming between charsets
   */
  public Rope(byte[] bytes, Charset charset) {
    int chunksize = Ropes.combinedLength;
    RopeLike root = null;
    for (int i = 0; i < bytes.length; i += chunksize) {
      byte[] bs = Arrays.copyOfRange(bytes, i, Math.min(i + chunksize, bytes.length));
      if (root == null) {
        root = new RopeLikeOverCharacterArray(Bytes.getCharacters(bs, charset));
      } else {
        root = root.append(
            new RopeLikeOverCharacterArray(Bytes.getCharacters(bs, charset)));
      }
    }
    base = root;
  }

  /**
   * construct a rope from a string.  If the string is longer than the default segment size, then
   * the rope will be decomposed into subropes of maximum length segment-size
   *
   * @param s
   */
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

  public int indexOf(String s) {
    return Strings.indexOf(base, s);
  }

  public Rope substring(int start, int end) {
    return new Rope(base.split(start).snd.split(end - start).fst);
  }
}
