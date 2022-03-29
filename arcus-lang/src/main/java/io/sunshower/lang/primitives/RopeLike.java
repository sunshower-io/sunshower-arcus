package io.sunshower.lang.primitives;

import lombok.NonNull;

interface RopeLike extends CharSequence {


  Rope asRope();

  enum Type {
    Composite, Flat,
  }

  int depth();
  Type getType();

  char[] characters();

  byte[] getBytes();

  RopeLike getLeft();

  RopeLike getRight();

  String substring(int offset, int length);

  int indexOf(final char ch);

  int indexOf(char ch, int startIndex);

  int indexOf(@NonNull CharSequence rope, int start);

  default int indexOf(@NonNull CharSequence rope) {
    return indexOf(rope, 0);
  }

}
