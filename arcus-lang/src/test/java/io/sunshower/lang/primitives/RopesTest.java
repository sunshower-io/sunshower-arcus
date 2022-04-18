package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledOnOs;
import org.junit.jupiter.api.condition.OS;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@DisabledOnOs(OS.WINDOWS)
class RopesTest {

  public static final String document2 =
      """
            @Override
            public void writeTree(PrintWriter out) {
              writeTree(out, this, "", true);
            }

            private void writeTree(PrintWriter out, RopeLike node, String indent, boolean last) {
              if (node.equals(this)) {
                out.append(node).append("\\n");
              } else {
                out.append(indent).append(last ? "|" : "").append(node).append("\\n");
              }
              indent = indent + (last ? "   " : "");
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
  static final byte[] bytes =
      readAllBytes(Path.of(ClassLoader.getSystemResource("longtest.txt").getFile()));
  private Rope rope;

  @SneakyThrows
  private static byte[] readAllBytes(Path of) {
    return Files.readAllBytes(of);
  }

  public static void print(Rope r) {
    val pw = new PrintWriter(System.out);
    r.base.writeTree(pw);
    pw.flush();
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "h", "he", "hello"})
  void ensurePrependingToSmallRopesWorks(String v) {
    val rope = new Rope(v);
    val r = rope.prepend("hello");
    assertEquals(r.toString(), "hello" + v);
  }

  @Test
  void ensureRemovingEntireRopeCreatesEmptyRope() {
    val rope = new Rope("hello world");
    assertEquals(0, rope.delete(0, rope.length()).length());
    assertEquals(rope.toString(), "hello world");
  }

  @Test
  void ensureIteratingOverLargeStringWorks() {
    val rope = new Rope(bytes);
    val iter = rope.base.iterator();
    while (iter.hasNext()) {
      val next = iter.next();
      assertTrue(next.isLeaf());
      assertTrue(next.getBytes().length <= Ropes.splitLength);
    }
  }

  @Test
  void ensureRemovingSubRopeWorks() {
    val rope = new Rope("hello world");

    val r = rope.delete(1, 2);
    assertEquals(rope.toString(), "hello world");
    assertEquals(r.toString(), "hlo world");
  }

  @Test
  void ensureRopeConstructorWorksForEmptyRope() {
    rope = new Rope();
    assertEquals(0, rope.length());
  }

  @Test
  void ensureRopeConstructorWorksForBytes() {
    rope = new Rope("hello world".getBytes());
    assertEquals(rope.toString(), "hello world");
  }

  @Test
  void ensureSubStringWorks() {
    val s = (document2);

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
  void ensureIterationWorks() {
    val r = new Rope(document2);
    val iter = r.base.iterator();
    val str = new StringBuilder();
    while (iter.hasNext()) {
      str.append(iter.next().characters());
    }
    assertEquals(r.toString(), str.toString());
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "h", "he", "hello"})
  void ensureShortStringsAreEqual(String v) {
    assertEquals(0, new Rope(v).compareTo(v));
  }

  @Test
  void ensureLongComparisonsAreEqual() {
    assertEquals(0, new Rope(document2).compareTo(document2));
  }

  @Test
  void ensureLexocographicalOrderingWorks() {
    val fst = "zebra";
    val snd = "calico";

    assertEquals(new Rope(fst).compareTo(snd), fst.compareTo(snd));
  }

  @Test
  void ensureIterationOnVeryLargeValueWorks() {
    val string = new String(bytes);
    val rope = new Rope(bytes);

    val str = new StringBuilder(bytes.length);
    val iter = rope.base.iterator();

    while (iter.hasNext()) {
      str.append(iter.next().toString());
    }
    assertEquals(str.toString(), string.toString());
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
    val rs = rope.base.split(rope.length() / 2);
    print(new Rope(rs.fst));
    print(new Rope(rs.snd));

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
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            this is a quick test--what do you think?
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            I think this is a pretty rad data-structure
            """);

    val rope = new Rope(s);
    val r = Ropes.nodeContaining(rope.base, rope.indexOf("data-structure") + 1);
    System.out.println(r);

    val writer = new PrintWriter(System.out);
    print(rope);

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
    print(rope);
    for (int i = 0; i < document2.length(); i++) {
      assertEquals(rope.charAt(i), document2.charAt(i));
    }
  }

  @Test
  void ensureRopeIsVisualizedCorrectly() {
    val s = document2;

    val rope = new Rope(s);

    assertEquals(rope.substring(10, 105).toString(), s.substring(10, 105));
  }

  @Test
  @SneakyThrows
  void ensureReadingRopeWorks() {
    rope = new Rope(bytes);
    val s = new StringBuilder(new String(bytes));

    val r = s.substring(400, 9000);
    val r1 = rope.substring(400, 9000);
    assertEquals(r, r1.toString());
  }

  @Test
  void ensurePrependingWorks() {
    val a = new Rope(bytes);
    val b = a.prepend("Sup world!");
    assertTrue(b.startsWith("Sup world!"));
  }

  @Test
  void ensureInsertWorks() {
    val test = new StringBuilder(new String(bytes)).insert(400, "Hello world!");
    val rope = new Rope(bytes).insert(400, "Hello world!");
    assertEquals(test.toString(), rope.toString());
  }

  @Test
  void testRopeAppend() {
    var rope = new Rope("103215412354123512354");
    var string = rope.toString();
    for (int i = 0; i < 100; i++) {
      rope = rope.append("world");
      string = string + "world";
    }
    assertEquals(string, rope.toString());
  }

  @Test
  void ensureInsertingCharacterAtEndOfRopeWorks() {
    val sb = new StringBuilder("hello");

    rope = new Rope(sb.toString());
    for (int i = 0; i < 100; i++) {
      sb.insert(sb.length(), "sup");
      rope = rope.insert(rope.length(), "sup");
    }
    assertEquals(sb.toString(), rope.toString());
  }

  @Test
  void testInsert() {
    var base = new String(" hello ");
    var stringBuffer = new StringBuilder(base);
    var rope = new Rope(base);
    for (int i = 0; i < 100; i++) {
      rope = rope.insert(rope.length() / 2, base);
      stringBuffer.insert(stringBuffer.length() / 2, base);
    }
    assertEquals(rope.toString(), stringBuffer.toString());
  }

  @Test
  void testRopePrepend() {
    var rope = new Rope("103215412354123512354");
    var string = new String(rope.toString());
    for (int i = 0; i < 15; i++) {
      rope = rope.prepend("Sup world!");
      string = "Sup world!" + string;
    }
    assertEquals(rope.toString(), string);
  }

  @Test
  void testStringPrepend() {
    for (int i = 0; i < 1000; i++) {
      val a = "Sup world!" + new String(bytes);
    }
  }
}
