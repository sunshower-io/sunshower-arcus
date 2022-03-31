package io.sunshower.lang.primitives;

import static io.sunshower.lang.primitives.Bytes.getCharacters;
import static io.sunshower.lang.primitives.Ropes.merge;
import static io.sunshower.lang.primitives.Ropes.rebalance;
import static java.util.Arrays.copyOfRange;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.nio.charset.Charset;
import java.util.ArrayList;
import lombok.NonNull;
import lombok.val;

/** implementation of the Rope data-structure */
public final class Rope implements CharSequence {

  /** the base of this rope */
  @NonNull final RopeLike base;

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
   * Construct a rope over the backing bytes. If the byte array is larger than the segment-length, a
   * deep rope will be created
   *
   * @param bytes the bytes to construct this over
   */
  @SuppressFBWarnings
  public Rope(@NonNull byte[] bytes) {
    this(bytes, Charset.defaultCharset());
  }

  /**
   * Construct the rope with the backing bytes
   *
   * @param bytes the bytes
   * @param charset the charset to use--only used for transforming between charsets
   */
  public Rope(byte[] bytes, Charset charset) {
    int chunksize = 17;
    RopeLike root = null;

    val leaves = new ArrayList<RopeLike>(bytes.length / chunksize);
    for (int i = 0; i < bytes.length; i += chunksize) {
      val subbytes = copyOfRange(bytes, i, Math.min(i + chunksize, bytes.length));
      leaves.add(new RopeLikeOverCharacterArray(getCharacters(subbytes, charset)));
    }
    base = rebalance(merge(leaves));
    //    for (int i = 0; i < bytes.length; i += chunksize) {
    //      val bs = copyOfRange(bytes, i, Math.min(i + chunksize, bytes.length));
    //      if (root == null) {
    //        root = new RopeLikeOverCharacterArray(getCharacters(bs, charset));
    //      } else {
    //        root = root.append(new RopeLikeOverCharacterArray(getCharacters(bs, charset)));
    //      }
    //    }
    //    assert root != null;
    //    base = root;
  }

  /**
   * construct a rope from a string. If the string is longer than the default segment size, then the
   * rope will be decomposed into subropes of maximum length segment-size
   *
   * @param s the string to construct this rope from
   */
  @SuppressFBWarnings
  public Rope(String s) {
    this(s.getBytes(), Charset.defaultCharset());
  }

  /**
   * construct a rope from the specified string and charset
   *
   * @param s the string
   * @param charset the charset to use
   */
  @SuppressFBWarnings
  public Rope(String s, Charset charset) {
    this(s.getBytes(), charset);
  }

  /** @return the length of this rope */
  @Override
  public int length() {
    return base.length();
  }

  /**
   * @param i the index of the character
   * @return
   */
  @Override
  public char charAt(int i) {
    return base.charAt(i);
  }

  /**
   * implements subsequence.
   *
   * @param start the start index
   * @param end the end index
   * @return the character sequence (an implementation of RopeLike)
   */
  @Override
  public CharSequence subSequence(int start, int end) {
    return substring(start, end);
  }

  /**
   * @param o the object to check for equality
   * @return true if this is equal to o, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o == this) {
      return true;
    }
    if (Rope.class.equals(o.getClass())) {
      val that = (Rope) o;
      return base.equals(that.base);
    }
    return false;
  }

  /** @return a hashcode compatible with String.hashcode */
  @Override
  public int hashCode() {
    return base.hashCode();
  }

  /**
   * convert this rope to a string. May cause an out-of-memory error for very large ropes
   *
   * @return this rope as a string
   */
  @Override
  public String toString() {
    return base.toString();
  }

  /**
   * locate the first occurrence of the specified string
   *
   * @param s the charsequence to locate
   * @return the index of the sequence, or -1 if not found
   */
  public int indexOf(CharSequence s) {
    return Strings.indexOf(base, s);
  }

  /**
   * @param start the start of the substring
   * @param end the end of the substring
   * @return the substring, or throw an exception for invalide ranges
   * @throws ArrayIndexOutOfBoundsException
   */
  public Rope substring(int start, int end) {
    return new Rope(base.split(start).snd.split(end - start).fst);
  }


  /**
   * insert the character sequence at location idx
   * @param idx the index to insert the location at
   * @param sequence the sequence to insert
   * @return a rope that is the result of inserting the sequence starting at location idx
   */
  public Rope insert(int idx, CharSequence sequence) {
    if(idx == 0) {
      return prepend(sequence);
    }
    if(idx == length()) {
      return append(sequence);
    }
    val lhs = base.split(idx);
    return new Rope(Ropes.append(lhs.fst.append(sequence), lhs.snd));
  }

  /**
   *
   * @param sequence the sequence to test
   * @return true if this starts with the sequence
   */
  public boolean startsWith(CharSequence sequence) {
    val length = length();
    if(length < sequence.length()) {
      return false;
    }
    for(int i = 0; i < sequence.length(); i++) {
      if(charAt(i) != sequence.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  /**
   * prepend the character sequence to this rope
   * @param sequence the sequence to prepend
   * @return a rope starting with the sequence and ending with this
   */
  public Rope prepend(CharSequence sequence) {
    return new Rope(base.prepend(sequence));
  }


  /**
   * append the character sequence to this rope
   * @param sequence the sequence to append
   * @return a rope starting with the sequence and ending with this
   */
  public Rope append(CharSequence sequence) {
    return new Rope(base.append(sequence));
  }
}
