package io.sunshower.lang.primitives;

import java.nio.charset.Charset;
import lombok.NonNull;
import lombok.val;

public class RopeLikeTree extends AbstractRopeLike implements RopeLike {

  private final int depth;
  private final int length;
  private final int weight;

  private final RopeLike left;
  private final RopeLike right;

  public RopeLikeTree(@NonNull final RopeLike left, @NonNull final RopeLike right) {
    this.left = left;
    this.right = right;
    val llen = left.length();
    this.weight = llen;
    this.length = llen + right.length();
    this.depth = Math.max(left.depth(), right.depth()) + 1;
  }

  @Override
  public Rope asRope() {
    return new Rope(this);
  }

  @Override
  public int weight() {
    return weight;
  }

  @Override
  public int depth() {
    return depth;
  }

  @Override
  public Type getType() {
    return Type.Composite;
  }

  @Override
  public char[] characters() {
    return new char[0];
  }

  @Override
  public byte[] getBytes() {
    return new byte[0];
  }

  @Override
  public byte[] getBytes(Charset charset) {
    return new byte[0];
  }

  @Override
  public RopeLike getLeft() {
    return left;
  }

  @Override
  public RopeLike getRight() {
    return right;
  }

  @Override
  public String substring(int offset, int length) {
    return null;
  }

  @Override
  public int indexOf(char ch) {
    int idx = left.indexOf(ch);
    if (idx != -1) {
      return idx;
    }
    return right.indexOf(ch);
  }

  @Override
  public int indexOf(char ch, int startIndex) {
    if (startIndex > left.length()) {
      return right.indexOf(ch, startIndex);
    }
    return left.indexOf(ch, startIndex);
  }

  @Override
  public int indexOf(@NonNull CharSequence rope, int start) {
    return Strings.indexOf(this, rope);
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public char charAt(int i) {
    if (i < 0 || i >= length) {
      throw new IndexOutOfBoundsException(
          "Index out of range: %d.  Max range: %d".formatted(i, length));
    }

    return i < left.length() ? left.charAt(i) : right.charAt(i - left.length());
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    Ropes.checkBounds(this, start, end);
    if (start == 0 && end == length()) {
      return this;
    }
    int leftLength = left.length();
    if (end <= leftLength) {
      return left.subSequence(start, end);
    }
    if (start >= leftLength) {
      return right.subSequence(start, leftLength);
    }
    return Ropes.append((RopeLike) left.subSequence(start, leftLength),
        (RopeLike) right.subSequence(0, end - leftLength));
  }

  @Override
  public String toString() {
    return left.toString() + right.toString();
  }
}
