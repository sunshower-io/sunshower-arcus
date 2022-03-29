package io.sunshower.lang.primitives;

import java.io.PrintWriter;
import java.nio.charset.Charset;
import lombok.NonNull;

interface RopeLike extends CharSequence {


  Rope asRope();

  default RopeLike append(CharSequence sequence) {
    return Ropes.append(this, new RopeLikeOverCharSequence(sequence));
  }

  enum Type {
    Composite, Flat,
  }

  int depth();
  Type getType();

  char[] characters();

  byte[] getBytes();
  byte[] getBytes(Charset charset);

  default RopeLike getLeft() {
    return null;
  }

  default RopeLike getRight() {
    return null;
  }

  String substring(int offset, int length);

  int indexOf(final char ch);

  int indexOf(char ch, int startIndex);

  int indexOf(@NonNull CharSequence rope, int start);

  default int indexOf(@NonNull CharSequence rope) {
    return indexOf(rope, 0);
  }

  void writeTree(PrintWriter out);



}
