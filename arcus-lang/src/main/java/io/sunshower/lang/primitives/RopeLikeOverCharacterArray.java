package io.sunshower.lang.primitives;

import static io.sunshower.lang.primitives.Arrays.remove;
import static io.sunshower.lang.primitives.Ropes.checkBounds;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.lang.tuple.Pair;
import java.util.Arrays;
import lombok.NonNull;
import lombok.val;

@SuppressFBWarnings
@SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
final class RopeLikeOverCharacterArray extends AbstractRopeLike {

  final char[] characters;

  RopeLikeOverCharacterArray(@NonNull final String chars) {
    this(chars.toCharArray());
  }

  RopeLikeOverCharacterArray(@NonNull char[] characters) {
    this(characters, 0, characters.length);
  }

  RopeLikeOverCharacterArray(@NonNull char[]... segments) {
    var length = 0;
    for (char[] segment : segments) {
      length += segment.length;
    }
    characters = new char[length];
    var destinationPos = 0;
    for (char[] segment : segments) {
      System.arraycopy(segment, 0, characters, destinationPos, segment.length);
      destinationPos += segment.length;
    }
  }

  RopeLikeOverCharacterArray(@NonNull final char[] sequence, final int offset, final int length) {
    checkBounds(sequence, offset);
    checkBounds(sequence, length);
    characters = new char[length];
    System.arraycopy(sequence, offset, characters, 0, length);
  }

  @Override
  public Rope asRope() {
    return new Rope(this);
  }

  @Override
  public int weight() {
    return characters.length;
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
    return characters;
  }

  @Override
  public RopeLike delete(int start, int length) {
    val chars = remove(characters, start, length);
    return new RopeLikeOverCharacterArray(chars);
  }

  @Override
  public Pair<RopeLike, RopeLike> split(int idx) {
    return Pair.of(
        new RopeLikeOverCharacterArray(Arrays.copyOfRange(characters, 0, idx)),
        new RopeLikeOverCharacterArray(Arrays.copyOfRange(characters, idx, length())));
  }

  @Override
  public String substring(int offset, int length) {
    return subSequence(offset, length).toString();
  }

  @Override
  public int indexOf(char ch) {
    val l = length();
    for (int i = 0; i < l; i++) {
      if (characters[i] == ch) {
        return i;
      }
    }
    return -1;
  }

  @Override
  public int indexOf(char ch, int start) {
    checkBounds(characters, start);
    val l = length();
    for (int i = start; i < l; i++) {
      if (characters[i] == l) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @param rope
   * @param start
   * @return
   */
  @Override
  public int indexOf(@NonNull CharSequence rope, int start) {
    return Strings.indexOf(this, rope);
  }

  @Override
  public RopeLikeOverCharacterArray clone() {
    return new RopeLikeOverCharacterArray(Arrays.copyOf(characters, characters.length));
  }

  @Override
  public int length() {
    return characters.length;
  }

  @Override
  public char charAt(int i) {
    return characters[i];
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    if (start == 0 && end == length()) {
      return this;
    }
    if (end - start < 16) {
      return new RopeLikeOverCharacterArray(characters, start, end - start);
    } else {
      return new RopeLikeOverString(this, start, end - start);
    }
  }

  @Override
  public String toString() {
    return new String(characters);
  }
}
