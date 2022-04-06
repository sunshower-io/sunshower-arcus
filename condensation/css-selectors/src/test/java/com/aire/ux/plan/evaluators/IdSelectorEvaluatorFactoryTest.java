package com.aire.ux.plan.evaluators;

import static com.aire.ux.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.test.Node;
import lombok.val;
import org.junit.jupiter.api.Test;

class IdSelectorEvaluatorFactoryTest extends EvaluatorFactoryTestCase {

  private Node node;

  @Test
  void ensureSimpleIdSelectionWorks() {
    val node = node("div").id("test");
    System.out.println(node);
    val result = parser.parse("div#test").plan(context).evaluate(node, Node.getAdapter());
    assertEquals(1, result.size());
  }

  @Test
  void ensureTypelessSelectorWorks() {
    val node = node("div").id("test");
    System.out.println(node);
    val result = parser.parse("#test").plan(context).evaluate(node, Node.getAdapter());
    assertEquals(1, result.size());
  }

  @Test
  void ensureDescedantSelectedByIdWorks() {
    node = node("html").child(node("body").children(node("span"), node("span").id("test")));
    val result = eval("span#test", node, Node.getAdapter());
    assertEquals(1, result.size());
    val n = at(result, 0);
    assertEquals("test", n.getAttribute("id"));
    assertEquals("span", n.getType());
  }

  @Test
  void ensureTypelessSelectionByIdWorks() {
    node = node("html").child(node("body").children(node("span"), node("span").id("test")));
    val result = eval("#test", node, Node.getAdapter());
    assertEquals(1, result.size());
    val n = at(result, 0);
    assertEquals("test", n.getAttribute("id"));
    assertEquals("span", n.getType());
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return new IdSelectorEvaluatorFactory();
  }
}
