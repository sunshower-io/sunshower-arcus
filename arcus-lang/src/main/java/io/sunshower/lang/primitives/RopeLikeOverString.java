package io.sunshower.lang.primitives;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.lang.tuple.Pair;
import java.nio.charset.Charset;
import lombok.NonNull;
import lombok.val;

@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
final class RopeLikeOverString extends AbstractRopeLike implements RopeLike {

  final String value;

  public RopeLikeOverString(@NonNull RopeLikeOverCharacterArray delegate, int offset, int length) {
    this.value = new String(delegate.characters, offset, length);
  }

  public RopeLikeOverString(char[] results) {
    this.value = new String(results);
  }

  RopeLikeOverString(final String value) {
    this.value = value;
  }

  @Override
  public int length() {
    return value.length();
  }

  @Override
  public char charAt(int i) {
    return value.charAt(i);
  }

  @Override
  public CharSequence subSequence(int start, int length) {
    return value.subSequence(start, length);
  }

  @Override
  public Rope asRope() {
    return new Rope(this);
  }

  @Override
  public int weight() {
    return value.length();
  }

  @Override
  public int depth() {
    return 0;
    //    return delegate.depth();
  }

  @Override
  public Type getType() {
    return Type.Flat;
  }

  @Override
  public char[] characters() {
    return value.toCharArray();
  }

  @Override
  public RopeLike delete(int start, int length) {
    val results = Arrays.remove(characters(), start, length);
    return new RopeLikeOverString(results);
    //    return delegate.delete(start, length);
  }

  @Override
  public Pair<RopeLike, RopeLike> split(int idx) {
    val chars = characters();
    val lhs = new String(chars, 0, idx);
    val rhs = new String(chars, idx, chars.length - idx);
    return Pair.of(new RopeLikeOverString(lhs), new RopeLikeOverString(rhs));
  }

  @Override
  @SuppressFBWarnings
  public byte[] getBytes() {
    return value.getBytes();
  }

  @Override
  public byte[] getBytes(Charset charset) {
    return value.getBytes(charset);
  }

  @Override
  public String substring(int offset, int length) {
    return value.substring(offset, length);
  }

  @Override
  public int indexOf(char ch) {
    return value.indexOf(ch);
  }

  @Override
  public int indexOf(char ch, int startIndex) {
    return value.indexOf(ch, startIndex);
  }

  @Override
  public int indexOf(@NonNull CharSequence rope, int start) {
    return value.indexOf(rope.toString(), start);
  }

  @Override
  @SuppressWarnings("PMD")
  public AbstractRopeLike clone() {
    return new RopeLikeOverString(value);
  }

  @Override
  public String toString() {
    return value;
  }


  @Override
  public RopeLikeOverString reverse() {
    val sbuilder = new StringBuilder(value);
    sbuilder.reverse();
    return new RopeLikeOverString(sbuilder.toString());
  }
}
