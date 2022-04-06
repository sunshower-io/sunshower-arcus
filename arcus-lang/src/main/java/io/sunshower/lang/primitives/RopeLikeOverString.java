package io.sunshower.lang.primitives;

import static io.sunshower.lang.primitives.Ropes.checkBounds;

import io.sunshower.lang.tuple.Pair;
import java.nio.charset.Charset;
import lombok.NonNull;

@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
final class RopeLikeOverString extends AbstractRopeLike implements RopeLike {

  private final int offset;
  private final int length;
  private final RopeLikeOverCharacterArray delegate;

  public RopeLikeOverString(@NonNull RopeLikeOverCharacterArray delegate, int offset, int length) {
    checkBounds(delegate, offset, offset);
    this.offset = offset;
    this.length = length;
    this.delegate = delegate;
  }

  @Override
  public int length() {
    return delegate.length();
  }

  @Override
  public char charAt(int i) {
    return delegate.charAt(i);
  }

  @Override
  public CharSequence subSequence(int start, int length) {
    return delegate.subSequence(start, length);
  }

  @Override
  public Rope asRope() {
    return delegate.asRope();
  }

  @Override
  public int weight() {
    return delegate.weight();
  }

  @Override
  public int depth() {
    return delegate.depth();
  }

  @Override
  public Type getType() {
    return delegate.getType();
  }

  @Override
  public char[] characters() {
    return delegate.characters();
  }

  @Override
  public RopeLike delete(int start, int length) {
    return delegate.delete(start, length);
  }

  @Override
  public Pair<RopeLike, RopeLike> split(int idx) {
    return delegate.split(idx);
  }

  @Override
  public byte[] getBytes() {
    return delegate.getBytes();
  }

  @Override
  public byte[] getBytes(Charset charset) {
    return delegate.getBytes(charset);
  }

  @Override
  public RopeLike getLeft() {
    return delegate.getLeft();
  }

  @Override
  public RopeLike getRight() {
    return delegate.getRight();
  }

  @Override
  public String substring(int offset, int length) {
    return delegate.substring(offset, length);
  }

  @Override
  public int indexOf(char ch) {
    return delegate.indexOf(ch);
  }

  @Override
  public int indexOf(char ch, int startIndex) {
    return delegate.indexOf(ch, startIndex);
  }

  @Override
  public int indexOf(@NonNull CharSequence rope, int start) {
    return delegate.indexOf(rope, start);
  }

  @Override
  @SuppressWarnings("PMD")
  public AbstractRopeLike clone() {
    return delegate.clone();
  }

  @Override
  public String toString() {
    return delegate.toString();
  }
}
