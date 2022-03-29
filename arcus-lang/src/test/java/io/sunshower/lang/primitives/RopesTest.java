package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.PrintWriter;
import lombok.val;
import org.junit.jupiter.api.Test;

class RopesTest {

  private Rope rope;

  @Test
  void ensureRopeConstructorWorksForEmptyRope() {
    rope = new Rope();
    assertEquals(0, rope.length());
  }

  @Test
  void ensureRopeConstructorWorksForBytes() {
    rope = new Rope("hello world".getBytes());
    assertEquals(rope, "hello world");
  }

  @Test
  void ensureSmallRopeIsVisualizedCorrectly() {
    val lhs = new RopeLikeOverCharacterArray("Hello");
    val rhs = new RopeLikeOverCharacterArray("World");
    val rope = new RopeLikeTree(lhs, rhs);
    val writer = new PrintWriter(System.out);
    rope.writeTree(writer);
    writer.flush();
  }

  @Test
  void ensureSmallConstructedRopeWorks() {
    val rope = new Rope("""
        this is a quick test--what do you think?
        """);
    val writer = new PrintWriter(System.out);
    rope.base.writeTree(writer);
    writer.flush();
  }

  @Test
  void ensureRopeIsVisualizedCorrectly() {
    val s =
        """
          @Override
          public void writeTree(PrintWriter out) {
            writeTree(out, this, "", true);
          }

          private void writeTree(PrintWriter out, RopeLike node, String indent, boolean last) {
            if (node.equals(this)) {
              out.append(node).append("\\n");
            } else {
              out.append(indent).append(last ? "└╴" : "├╴").append(node).append("\\n");
            }
            indent = indent + (last ? "   " : "│  ");
            val results = new ArrayList<RopeLike>();
            if (getLeft() != null) {
              results.add(getLeft());
            }
            if (getRight() != null) {
              results.add(getRight());
            }

            val iter = results.iterator();
            while (iter.hasNext()) {
              val child = iter.next();
              val isLast = !iter.hasNext();
              writeTree(out, child, indent, isLast);
            }
          }

          @Override
          public int hashCode() {
            int h = 0;
            int len = length();
            for (int i = 0; i < len; i++) {
              h = 31 * h + charAt(i);
            }
            return h;
          }

          @Override
          public boolean equals(Object o) {
            if (o == null) {
              return false;
            }
            if (o == this) {
              return true;
            }

            if (o instanceof CharSequence seq) {
              if (length() != seq.length()) {
                return false;
              }
              for (int i = 0; i < length(); i++) {
                if (charAt(i) != seq.charAt(i)) {
                  return false;
                }
              }
              return true;
            }
            return false;
          }

        """;

    val rope = new Rope(s);
    val writer = new PrintWriter(System.out);
    rope.base.writeTree(writer);
    writer.flush();
  }
}
