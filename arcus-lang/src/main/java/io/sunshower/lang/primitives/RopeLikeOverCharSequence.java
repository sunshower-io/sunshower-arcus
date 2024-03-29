package io.sunshower.lang.primitives;

import io.sunshower.lang.tuple.Pair;
import lombok.NonNull;
import lombok.val;

final class RopeLikeOverCharSequence extends AbstractRopeLike {

  private final CharSequence delegate;

  RopeLikeOverCharSequence(@NonNull CharSequence sequence) {
    this.delegate = sequence;
  }

  @Override
  public Rope asRope() {
    return new Rope(this);
  }

  @Override
  public int weight() {
    return delegate.length();
  }

  @Override
  public int depth() {
    return 0;
  }

  @Override
  public Type getType() {
    return Type.Flat;
  }

  @Override
  public char[] characters() {
    val len = length();
    val chars = new char[len];
    for (int i = 0; i < len; i++) {
      chars[i] = charAt(i);
    }
    return chars;
  }

  @Override
  public RopeLike delete(int start, int length) {
    val len = length();
    val result = new StringBuilder(len - length);
    result.append(delegate, 0, start);
    result.append(delegate, start + length, len - (start + length));
    return new RopeLikeOverCharSequence(result);
  }

  public Pair<RopeLike, RopeLike> split(int idx) {
    return Pair.of(
        new RopeLikeOverCharSequence(subSequence(0, idx)),
        new RopeLikeOverCharSequence(subSequence(idx, length())));
  }

  @Override
  public int indexOf(char ch) {
    return indexOf(ch, 0);
  }

  @Override
  public int indexOf(char ch, int startIndex) {
    for (int i = startIndex; i < length(); i++) {
      if (charAt(i) == ch) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int indexOf(@NonNull CharSequence sequence, int start) {
    return Strings.indexOf(this, sequence);
  }

  @Override
  public RopeLikeOverCharSequence clone() {
    return new RopeLikeOverCharSequence(subSequence(0, length()));
  }

  @Override
  public int length() {
    return delegate.length();
  }

  @Override
  public char charAt(int i) {
    return delegate.charAt(i);
  }

  /**
   * @param i
   * @param i1
   * @return
   */
  @Override
  public CharSequence subSequence(int i, int i1) {
    return delegate.subSequence(i, i1);
  }

  public String toString() {
    return delegate.toString();
  }

  @Override
  public RopeLikeOverCharSequence reverse() {
    val result = new StringBuilder(delegate);
    result.reverse();
    return new RopeLikeOverCharSequence(result);
  }
}
