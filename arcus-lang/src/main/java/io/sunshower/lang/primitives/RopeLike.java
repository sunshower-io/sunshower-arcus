package io.sunshower.lang.primitives;

import io.sunshower.lang.tuple.Pair;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import lombok.NonNull;
import lombok.val;

interface RopeLike extends CharSequence, Cloneable, Iterable<RopeLike> {

  Rope asRope();

  default RopeLike prepend(CharSequence sequence) {
    return Ropes.concat(new RopeLikeOverCharSequence(sequence), this);
  }

  default RopeLike append(CharSequence sequence) {
    return Ropes.concat(this, new RopeLikeOverCharSequence(sequence));
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
    return getBytes(StandardCharsets.UTF_8);
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

  default List<RopeLike> leaves() {
    return Ropes.collectLeaves(this);
  }

  default Iterator<RopeLike> iterator() {
    return new InOrderRopeIterator(this);
  }

  default Iterator<RopeLike> reverseIterator() {
    return new ReverseOrderRopeIterator(this);
  }

  default RopeLike reverse() {
    val reverse = reverseIterator();
    val list = new ArrayList<RopeLike>();
    while (reverse.hasNext()) {
      list.add(reverse.next().reverse());
    }
    return Ropes.merge(list);
  }

  enum Type {
    Composite,
    Flat,
  }
}

final class ReverseOrderRopeIterator implements Iterator<RopeLike> {

  private final Deque<RopeLike> stack;

  ReverseOrderRopeIterator(@NonNull RopeLike root) {
    this.stack = new ArrayDeque<>();
    var c = root;
    while (c != null) {
      stack.push(c);
      c = c.getRight();
    }
  }

  @Override
  public boolean hasNext() {
    return !stack.isEmpty();
  }

  @Override
  public RopeLike next() {
    val result = stack.pop();
    if (!stack.isEmpty()) {
      val parent = stack.pop();
      val left = parent.getLeft();
      if (left != null) {
        stack.push(left);
        var cright = left.getRight();
        while (cright != null) {
          stack.push(cright);
          cright = cright.getRight();
        }
      }
    }
    return result;
  }
}

final class InOrderRopeIterator implements Iterator<RopeLike> {

  private final Deque<RopeLike> stack;

  InOrderRopeIterator(@NonNull RopeLike root) {
    stack = new ArrayDeque<>();
    var c = root;
    while (c != null) {
      stack.push(c);
      c = c.getLeft();
    }
  }

  @Override
  public boolean hasNext() {
    return stack.size() > 0;
  }

  @Override
  public RopeLike next() {

    val result = stack.pop();

    if (!stack.isEmpty()) {
      val parent = stack.pop();
      val right = parent.getRight();
      if (right != null) {
        stack.push(right);
        var cleft = right.getLeft();
        while (cleft != null) {
          stack.push(cleft);
          cleft = cleft.getLeft();
        }
      }
    }
    return result;
  }
}
