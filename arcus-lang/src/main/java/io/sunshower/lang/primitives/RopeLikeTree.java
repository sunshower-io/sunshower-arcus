package io.sunshower.lang.primitives;

import java.nio.charset.Charset;
import lombok.NonNull;

public class RopeLikeTree implements RopeLike {

  private final int depth;
  private final int length;

  private final RopeLike left;
  private final RopeLike right;

  public RopeLikeTree(@NonNull final RopeLike left, @NonNull final RopeLike right) {
    this.left = left;
    this.right = right;
    this.length = left.length() + right.length();
    this.depth = Math.max(left.depth(), right.depth()) + 1;
  }


  @Override
  public Rope asRope() {
    return null;
  }

  @Override
  public int depth() {
    return 0;
  }

  @Override
  public Type getType() {
    return null;
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
    return null;
  }

  @Override
  public RopeLike getRight() {
    return null;
  }

  @Override
  public String substring(int offset, int length) {
    return null;
  }

  @Override
  public int indexOf(char ch) {
    return 0;
  }

  @Override
  public int indexOf(char ch, int startIndex) {
    return 0;
  }

  @Override
  public int indexOf(@NonNull CharSequence rope, int start) {
    return 0;
  }

  @Override
  public int length() {
    return 0;
  }

  @Override
  public char charAt(int i) {
    return 0;
  }

  @Override
  public CharSequence subSequence(int i, int i1) {
    return null;
  }
}
