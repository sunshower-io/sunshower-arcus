package io.sunshower.lang.primitives;

import io.sunshower.lang.primitives.RopeLike.Type;
import lombok.NonNull;
import lombok.val;

final class Ropes {

  static final long[] FIBONACCI_SEQUENCE = Longs.computeFibonacciUntil(
      7540113804746346429l);

  private Ropes() {

  }

  static void checkLengthSum(long max, RopeLike... ropes) {
    long sum = 0;
    if (ropes.length == 2) {
      sum = ropes[0].length() + ropes[1].length();
    }
    for (val rope : ropes) {
      sum += rope.length();
    }
    if (sum > max) {
      throw new IllegalArgumentException("Resulting rope cannot fit into a character sequence");
    }
  }

  static Rope concatenate(final RopeLike left, final RopeLike right) {
    if (left.isEmpty()) {
      return right.asRope();
    }
    if (right.isEmpty()) {
      return left.asRope();
    }
    checkLengthSum(Integer.MAX_VALUE, left, right);
    return null;

//    if (left.getType() != Type.Composite) {
//      if (right.getType() == Type.Composite) {
//        return rebalance(
//            new CompositeRole(new FlatRope(left.characters(), right.getLeft().characters())))
//      }
//    }
//
  }


  static void checkBounds(@NonNull final char[] sequence, final int bound) {
    if (bound < 0 || bound >= sequence.length) {
      throw new IllegalArgumentException(
          "Bound (%d) must be within [%d, %d]".formatted(bound, 0, sequence.length));
    }
  }
}
