package io.sunshower.lang.primitives;

import static io.sunshower.lang.primitives.Ropes.checkBounds;

import java.nio.charset.Charset;
import lombok.NonNull;
import lombok.val;

final class RopeLikeOverCharacterArray extends AbstractRopeLike {

  final char[] characters;

  RopeLikeOverCharacterArray(final String chars) {
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
    return null;
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
  public byte[] getBytes() {
    return getBytes(Charset.defaultCharset());
  }

  @Override
  public byte[] getBytes(@NonNull Charset charset) {
    return Strings.getBytes(characters, charset);

  }


  @Override
  public String substring(int offset, int length) {
    return null;
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
   * implementation of the Raita algorithm
   *
   * @param rope
   * @param start
   * @return
   */
  @Override
  public int indexOf(@NonNull CharSequence rope, int start) {
    return 0;
  }

  @Override
  public int length() {
    return characters.length;
  }

  @Override
  public char charAt(int i) {
    checkBounds(characters, i);
    return characters[i];
  }

  @Override
  public CharSequence subSequence(int start, int end) {
    if (start == 0 && end == length()) {
      return this;
    }
    if (end - start < 16) {
      return new RopeLikeOverCharacterArray(characters, start,
          end - start);
    } else {
      return new RopeLikeOverString(this, start, end - start);
    }
  }

  @Override
  public String toString() {
    return new String(characters);
  }
}
