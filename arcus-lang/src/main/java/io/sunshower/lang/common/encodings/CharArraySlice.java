package io.sunshower.lang.common.encodings;

import lombok.val;

public class CharArraySlice implements CharSequence {

  final char[] input;
  private final int length;
  private final int start;

  public CharArraySlice(char[] input, int start, int length) {
    this.length = length;
    this.start = start;
    this.input = input;
  }

  public CharArraySlice(char[] input) {
    this(input, 0, input.length);
  }

  @Override
  public int length() {
    return length;
  }

  @Override
  public char charAt(int i) {
    if (i >= start && i < length) {
      return input[start + i];
    }
    throw new IndexOutOfBoundsException(
        String.format("Index %d of bounds: %d, %d", i, start, length));
  }

  @Override
  public CharSequence subSequence(int i, int i1) {
    return new CharArraySlice(input, start + i, i1 - i);
  }

  @Override
  public String toString() {
    return new String(input, start, length);
  }

  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o == this) {
      return false;
    }

    if (o instanceof CharArraySlice) {
      val cslice = (CharArraySlice) o;
      val cinput = cslice.input;
      if (cinput.length != length) {
        return false;
      }

      for (int i = 0; i < length; i++) {
        if (charAt(i) != cslice.charAt(i)) {
          return false;
        }
      }
    }
    return true;
  }
}
