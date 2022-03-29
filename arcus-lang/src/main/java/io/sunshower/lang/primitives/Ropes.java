package io.sunshower.lang.primitives;

import io.sunshower.lang.primitives.RopeLike.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import lombok.NonNull;
import lombok.val;

final class Ropes {

  static final int MAX_DEPTH = 96;
  static final RopeLike EMPTY = new RopeLikeOverCharacterArray();

  static final int combinedLength = 17;
  static final long[] FIBONACCI_SEQUENCE = Longs.computeFibonacciUntil(7540113804746346429l);

  private Ropes() {}

  static RopeLike append(RopeLike left, RopeLike right) {
    if (left.isEmpty()) {
      return right;
    }
    if (right.isEmpty()) {
      return left;
    }
    checkLengthSum(Integer.MAX_VALUE, left, right);
    if (left.getType() != Type.Composite) {
      if (right.getType() == Type.Composite) {
        val rightChild = right.getLeft();
        assert rightChild != null;
        if (left.length() + rightChild.length() < combinedLength) {
          return rebalance(
              new RopeLikeTree(
                  new RopeLikeOverCharacterArray(left.characters(), right.characters()),
                  right.getRight()));
        }
      }
    }

    if (right.getType() != Type.Composite) {
      if (left.getType() == Type.Composite) {
        val leftChild = left.getRight();
        if (right.length() + leftChild.length() < combinedLength) {
          return rebalance(
              new RopeLikeTree(
                  left.getLeft(),
                  new RopeLikeOverCharacterArray(leftChild.characters(), right.characters())));
        }
      }
    }
    return rebalance(new RopeLikeTree(left, right));
  }

  static boolean isBalanced(RopeLike r) {
    val depth = r.depth();
    if (depth >= FIBONACCI_SEQUENCE.length - 2) {
      return false;
    }
    return FIBONACCI_SEQUENCE[depth + 2] <= r.length();
  }

  static RopeLike rebalance(RopeLike r) {
    if (r.depth() > MAX_DEPTH) {
      val leaves = new ArrayList<RopeLike>();
      val stack = new ArrayDeque<RopeLike>();
      stack.push(r);
      while (!stack.isEmpty()) {
        val rope = stack.pop();
        val left = rope.getLeft();
        val right = rope.getRight();
        if (left == null && right == null) {
          leaves.add(rope);
        }
        if (left != null) {
          stack.push(left);
        }
        if (right != null) {
          stack.push(right);
        }
      }
      return merge(leaves, 0, leaves.size());
    }
    return r;
  }

  static RopeLike merge(List<RopeLike> leaves, int start, int end) {
    int range = end - start;
    if (range == 1) {
      return leaves.get(start);
    }
    if (range == 2) {
      return new RopeLikeTree(leaves.get(start), leaves.get(start + 1));
    }
    int mid = start + (range / 2);
    return new RopeLikeTree(merge(leaves, start, mid), merge(leaves, mid, end));
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

  static void checkBounds(@NonNull final RopeLike rope, final int offset, final int length) {
    if (length < 0 || offset < 0 || offset + length > rope.length()) {
      throw new IllegalArgumentException(
          "Bound (offset: %d,  length: %d) must be within [%d, %d]"
              .formatted(offset, length, 0, rope.length()));
    }
  }

  static void checkBounds(@NonNull final char[] sequence, final int bound) {
    if (bound < 0 || bound > sequence.length) {
      throw new IllegalArgumentException(
          "Bound (%d) must be within [%d, %d]".formatted(bound, 0, sequence.length));
    }
  }
}
