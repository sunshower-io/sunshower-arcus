package io.sunshower.lang.primitives;

import static io.sunshower.lang.primitives.Bytes.getCharacters;
import static io.sunshower.lang.primitives.Ropes.merge;
import static java.util.Arrays.copyOfRange;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Consumer;
import lombok.NonNull;
import lombok.val;

/**
 * implementation of the Rope data-structure
 */
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
public final class Rope implements CharSequence, Comparable<CharSequence>, Iterable<CharSequence> {

  /**
   * the base of this rope
   */
  @NonNull
  final RopeLike base;
  /**
   * cache this. Don't check if it's zero--that basically never happens
   */
  private int hashcode;

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
   * @param bytes   the bytes
   * @param charset the charset to use--only used for transforming between charsets
   */
  public Rope(byte[] bytes, Charset charset) {
    this(bytes, 0, bytes.length, charset);
  }


  public Rope(byte[] bytes, int from, int length) {
    this(bytes, from, length, Charset.defaultCharset());
  }

  public Rope(byte[] bytes, int from, int length, Charset charset) {
    val chunksize = Ropes.splitLength;
    val len = length;
    if (len < Ropes.splitLength) {
      base = new RopeLikeOverString(new String(bytes, from, length, charset));
    } else {
      val leaves = new ArrayList<RopeLike>(len / chunksize);
      for (int i = from; i < len; i += chunksize) {
        val subbytes = copyOfRange(bytes, i, Math.min(i + chunksize, len));
        leaves.add(new RopeLikeOverString(getCharacters(subbytes, charset)));
      }
      base = merge(leaves);
    }
  }

  public Rope(CharSequence sequence) {
    val chunksize = Ropes.splitLength;
    if (sequence.length() < Ropes.splitLength) {
      base = new RopeLikeOverCharSequence(sequence);
    } else {
      val len = sequence.length();
      val leaves = new ArrayList<RopeLike>(len / chunksize);
      for (int i = 0; i < len; i += chunksize) {
        val subbytes = sequence.subSequence(i, Math.min(i + chunksize, len));
        leaves.add(new RopeLikeOverCharSequence(subbytes));
      }
      base = merge(leaves);
    }

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
   * @param s       the string
   * @param charset the charset to use
   */
  @SuppressFBWarnings
  public Rope(String s, Charset charset) {
    this(s.getBytes(), charset);
  }


  public static byte[] toByteArray(CharSequence leaf) {
    return toByteArray(leaf, Charset.defaultCharset());
  }

  public static byte[] toByteArray(CharSequence leaf, Charset charset) {
//    if(leaf instanceof RopeLike) {
//      return ((RopeLike) leaf).getBytes(charset);
//    }
    val cb = CharBuffer.wrap(leaf);
    return charset.encode(cb).array();
  }

  /**
   * @return the length of this rope
   */
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
   * @return a character sequence that iterates over the characters quickly
   */
  public CharSequence sequentialCharacters() {
    val len = this.length();
    return new CharSequence() {
      final Iterator<CharSequence> iterator = Rope.this.iterator();
      int currentIndex = 0,
          currentMax = 0,
          previousIndex = -1;
      CharSequence currentSequence;

      @Override
      public int length() {
        return len;
      }

      @Override
      public char charAt(int i) {
        if (i != previousIndex + 1) {
          return Rope.this.charAt(i);
        }
        if (currentSequence == null || i >= currentMax) {
          currentSequence = iterator.next();
          currentMax += currentSequence.length();
          currentIndex = 0;
        }

        previousIndex++;
        return currentSequence.charAt(currentIndex++);
      }

      @Override
      public CharSequence subSequence(int i, int i1) {
        return Rope.this.subSequence(i, i1);
      }
    };

  }

  /**
   * implements subsequence.
   *
   * @param start the start index
   * @param end   the end index
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
      return compareTo(that) == 0;
    }
    return false;
  }

  /**
   * iterate over the leaves of this rope.  This may be faster than
   *
   * @param consumer the consumer to apply to each leaf of this rope
   */
  public void forEachLeaf(Consumer<CharSequence> consumer) {
    val iter = base.iterator();
    while (iter.hasNext()) {
      val next = iter.next();
      for (int i = 0; i < next.length(); i++) {
        consumer.accept(next);
      }
    }
  }

  /**
   * iterate over this rope character by character, applying the consumer this may be faster than
   * iterating from 0 to this.length() and calling charAt()
   *
   * @param consumer the character consumer
   */
  public void forEachCharacter(CharConsumer consumer) {
    val iter = base.iterator();
    while (iter.hasNext()) {
      val next = iter.next();
      for (int i = 0; i < next.length(); i++) {
        consumer.accept(next.charAt(i));
      }
    }
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
   * @param end   the end of the substring
   * @return the substring, or throw an exception for invalide ranges
   * @throws ArrayIndexOutOfBoundsException
   */
  public Rope substring(int start, int end) {
    return new Rope(base.split(start).snd.split(end - start).fst);
  }

  /**
   * insert the character sequence at location idx
   *
   * @param idx      the index to insert the location at
   * @param sequence the sequence to insert
   * @return a rope that is the result of inserting the sequence starting at location idx
   */
  public Rope insert(int idx, CharSequence sequence) {
    if (idx == 0) {
      return prepend(sequence);
    }
    if (idx == length()) {
      return append(sequence);
    }
    val lhs = base.split(idx);
    return new Rope(Ropes.concat(lhs.fst.append(sequence), lhs.snd));
  }

  /**
   * @param sequence the sequence to test
   * @return true if this starts with the sequence
   */
  public boolean startsWith(CharSequence sequence) {
    val length = length();
    if (length < sequence.length()) {
      return false;
    }
    for (int i = 0; i < sequence.length(); i++) {
      if (charAt(i) != sequence.charAt(i)) {
        return false;
      }
    }
    return true;
  }

  /**
   * prepend the character sequence to this rope
   *
   * @param sequence the sequence to prepend
   * @return a rope starting with the sequence and ending with this
   */
  public Rope prepend(CharSequence sequence) {
    return new Rope(base.prepend(sequence));
  }

  /**
   * append the character sequence to this rope
   *
   * @param sequence the sequence to append
   * @return a rope starting with the sequence and ending with this
   */
  public Rope append(CharSequence sequence) {
    return new Rope(base.append(sequence));
  }

  /**
   * remove the section between start (inclusive) and end (exclusive) from this rope to create a new
   * rope
   *
   * @param start the start index
   * @param end   the end index
   * @return
   */
  public Rope delete(int start, int end) {
    return new Rope(base.delete(start, end));
  }

  /**
   * lexocographically compare this rope to the other rope. This method uses a lazy, in-order
   * optimization
   *
   * @param sequence
   * @return the result of the lexicographical comparison
   */
  @Override
  @SuppressWarnings("PMD.CompareObjectsWithEquals")
  public int compareTo(CharSequence sequence) {
    val iterator = base.iterator();
    int ch = 0;
    while (iterator.hasNext()) {
      val subsequence = iterator.next();
      for (int i = 0; i < subsequence.length(); i++) {
        val tch = sequence.charAt(ch++);
        val mch = subsequence.charAt(i);
        if (tch != mch) {
          return mch - tch;
        }
      }
    }
    return 0;
  }

  /**
   * @return a hashcode compatible with String.hashcode
   */
  @Override
  public int hashCode() {
    if (hashcode == 0) {
      val iterator = base.iterator();
      int h = 0;
      while (iterator.hasNext()) {
        val subsequence = iterator.next();
        for (int i = 0; i < subsequence.length(); i++) {
          h = 31 * h + subsequence.charAt(i);
        }
      }
      return (hashcode = h);
    }
    return hashcode;
  }

  @Override
  public Iterator<CharSequence> iterator() {
    val iterator = base.iterator();
    return new Iterator<>() {
      @Override
      public boolean hasNext() {
        return iterator.hasNext();
      }

      @Override
      public CharSequence next() {
        return iterator.next();
      }
    };
  }
}
