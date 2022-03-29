package io.sunshower.lang.primitives;

import static io.sunshower.lang.primitives.Ropes.checkBounds;

import lombok.NonNull;
import lombok.val;

public abstract class AbstractRopeLike implements RopeLike {


  static final class RopeLikeOverCharacterArray extends AbstractRopeLike {

    final char[] characters;

    RopeLikeOverCharacterArray(@NonNull char[] characters) {
      this(characters, 0, characters.length);
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
      val l = length();
      for(int i = 0; i < l; i++) {
        if(characters[i] == ch) {
          return i;
        }
      }
      return -1;
    }

    @Override
    public int indexOf(char ch, int start) {
      checkBounds(characters, start);
      val l = length();
      for(int i = start; i < l; i++) {
        if(characters[i] == l) {
          return i;
        }
      }
      return -1;
    }


    /**
     * implementation of the Raita algorithm
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
      return 0;
    }

    @Override
    public CharSequence subSequence(int i, int i1) {
      return null;
    }
  }


}