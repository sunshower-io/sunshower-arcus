package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import lombok.val;
import org.junit.jupiter.api.Test;

class RopesTest {

  public static final String document2 = """
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
  private Rope rope;

  public static void print(Rope r) {
    val pw = new PrintWriter(System.out);
    r.base.writeTree(pw);
    pw.flush();
  }

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
  void ensureSubStringWorks() {
    val s =
        (document2);

    val r = new Rope(s);
    val r1 = r.subSequence(15, 200);
    val s1 = s.subSequence(15, 200);
    assertEquals(r1.toString(), s1.toString());
  }

  @Test
  void ensureSmallRopeIsVisualizedCorrectly() {
    val lhs = new RopeLikeOverCharacterArray("Hello");
    val rhs = new RopeLikeOverCharacterArray("World");
    val rope = new RopeLikeTree(lhs, rhs);
    assertArrayEquals("HelloWorld".toCharArray(), rope.characters());
    val writer = new PrintWriter(System.out);
    rope.writeTree(writer);
    writer.flush();
  }

  @Test
  void ensureRopeSubsequenceWorks() {
    val s =
        ("""
            this is a quick test--what do you think?
            I think this is a pretty rad data-structure
            """);

    val rope = new Rope(s);
    assertTrue(Ropes.isBalanced(rope.base));
    assertEquals(s.subSequence(4, 14).toString(), rope.subSequence(4, 14).toString());
  }

  @Test
  void ensureSmallConstructedRopeWorks() {
    val rope =
        new Rope(
            """
                this is a quick test--what do you think?
                I think this is a pretty rad data-structure

                adfafdafadfdf

                lolo

                """);
    val writer = new PrintWriter(System.out);
    rope.base.writeTree(writer);
    writer.flush();
  }

  @Test
  void ensureWeightsWorkAtFirstSecondLevel() {
    val lhs = new RopeLikeOverCharacterArray("hello");
    val rhs = new RopeLikeOverCharacterArray("1");

    val parent = new RopeLikeTree(lhs, rhs);
    assertEquals(lhs.weight(), parent.weight());

    val lhs1 = new RopeLikeOverCharacterArray("sup");
    val gparent = new RopeLikeTree(lhs1, lhs);
    assertEquals(lhs1.weight(), gparent.weight());
  }

  @Test
  void ensureWeightsWorkAtFirstLevel() {
    val lhs = new RopeLikeOverCharacterArray("hello");
    val rhs = new RopeLikeOverCharacterArray("1");

    val parent = new RopeLikeTree(lhs, rhs);
    assertEquals(lhs.weight(), parent.weight());
  }

  @Test
  void ensureSplitWorks() {

    val s =
        ("""
            this is a quick test--what do you think?
            I think this is a pretty rad data-structure
            """);

    val rope = new Rope(s);
    val r = Ropes.nodeContaining(rope.base, rope.indexOf("data-structure") + 1);
    System.out.println(r);

    val writer = new PrintWriter(System.out);
    r.writeTree(writer);
    writer.flush();
  }

  @Test
  void ensureTestWorks() {
    val r = new Rope("Hello my name is Simon");
    assertEquals(r.length(), 22);
    val pr = new PrintWriter(System.out);
    r.base.writeTree(pr);
    pr.flush();
  }

  @Test
  void ensureCharAtWorksForLongString() {
    val rope = new Rope(document2);
    for(int i = 0; i < document2.length(); i++) {
      assertEquals(rope.charAt(i), document2.charAt(i));
    }
  }

  @Test
  void ensureRopeIsVisualizedCorrectly() {
    val s =
        document2;

    val rope = new Rope(s);

    assertEquals(rope.substring(10, 105).toString(), s.substring(10, 105));
  }
}
