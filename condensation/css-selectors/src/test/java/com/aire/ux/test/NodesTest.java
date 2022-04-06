package com.aire.ux.test;

import static com.aire.ux.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.Test;

class NodesTest {

  @Test
  void ensureGetSucceedingSiblingsWorks() {
    val r = node("parent").children(node("c1"), node("c2"), node("c3"));

    val p = r.getChildren().get(1);
    val children = Node.getAdapter().getSucceedingSiblings(p);
    assertEquals(1, children.size());
    assertEquals("c3", children.get(0).getType());
  }

  @Test
  void ensureGetPreceedingSiblingsWorks() {
    val r = node("parent").children(node("c1"), node("c2"), node("c3"));

    val p = r.getChildren().get(2);
    val children = Node.getAdapter().getPreceedingSiblings(p);
    assertEquals(2, children.size());
    for (int i = 0; i < children.size(); i++) {
      assertEquals("c" + (i + 1), children.get(i).getType());
    }
  }

  @Test
  void ensureParentChildApiMakesSense() {
    val r =
        node("p")
            .attribute("class", "red")
            .children(
                node("a")
                    .attribute("href", "www.google.com")
                    .children(node("h1").content("hello world, how " + "\nare you?")));
    val result = Node.getAdapter().reduce(r, 0, (c, n) -> n + 1);
    assertEquals(3, result);
  }

  @Test
  void ensureParentRetrievalWorks() {
    val node = node("parent").child(node("child"));
    assertEquals(node, node.getChildren().get(0).getParent());
  }

  @Test
  void ensureMappingWorks() {
    val r =
        node("p")
            .attribute("class", "red")
            .children(
                node("a")
                    .attribute("href", "www.google.com")
                    .children(node("h1").content("hello world, how " + "\nare you?")));
    val adapter = Node.getAdapter();

    var count =
        Node.getAdapter().reduce(r, 0, (c, n) -> adapter.hasAttribute(c, "hello") ? n + 1 : n);
    assertEquals(0, count);
    val t =
        Node.getAdapter()
            .map(r, Node.getAdapter(), (node, hom) -> hom.setAttribute(node, "hello", "world"));
    count = Node.getAdapter().reduce(t, 0, (c, n) -> adapter.hasAttribute(c, "hello") ? n + 1 : n);
    assertEquals(3, count);
    System.out.println(t);
  }
}
