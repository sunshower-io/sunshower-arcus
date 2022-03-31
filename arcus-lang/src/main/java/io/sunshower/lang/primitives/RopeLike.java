package io.sunshower.lang.primitives;

import io.sunshower.lang.tuple.Pair;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import lombok.NonNull;
import lombok.val;

interface RopeLike extends CharSequence, Cloneable {

  Rope asRope();


  default RopeLike prepend(CharSequence sequence) {
    return Ropes.append(new RopeLikeOverCharSequence(sequence), this);
  }

  default RopeLike append(CharSequence sequence) {
    return Ropes.append(this, new RopeLikeOverCharSequence(sequence));
  }

  default boolean isLeaf() {
    return getLeft() == null && getRight() == null;
  }

  @SuppressWarnings("PMD.ReturnEmptyArrayRatherThanNull")
  default char[] characters() {
    if (!isLeaf()) {
      /**
       * we should be balanced, so any non-leaf will have both a left and a right. If not, throw the
       * NPE and report the bug with the tree
       */
      val left = getLeft();
      assert left != null;
      val right = getRight();
      assert right != null;
      val leftChars = left.characters();

      assert leftChars != null;
      val rightChars = right.characters();
      assert rightChars != null;
      val leftLen = leftChars.length;
      val rightLen = rightChars.length;

      val newChars = new char[leftLen + rightLen];
      System.arraycopy(leftChars, 0, newChars, 0, leftLen);
      System.arraycopy(rightChars, 0, newChars, leftLen, rightLen);
      return newChars;
    }
    return null;
  }

  RopeLike delete(int start, int length);

  Pair<RopeLike, RopeLike> split(int idx);

  int weight();

  int depth();

  Type getType();

  default byte[] getBytes() {
    return getBytes(Charset.defaultCharset());
  }

  default byte[] getBytes(Charset charset) {
    return Strings.getBytes(characters(), charset);
  }

  default RopeLike getLeft() {
    return null;
  }

  default RopeLike getRight() {
    return null;
  }

  default String substring(int offset, int length) {
    return subSequence(offset, length).toString();
  }

  int indexOf(final char ch);

  int indexOf(char ch, int startIndex);

  int indexOf(@NonNull CharSequence rope, int start);

  default int indexOf(@NonNull CharSequence rope) {
    return indexOf(rope, 0);
  }

  void writeTree(PrintWriter out);

  @SuppressWarnings("PMD")
  RopeLike clone();

  enum Type {
    Composite,
    Flat,
  }
}
