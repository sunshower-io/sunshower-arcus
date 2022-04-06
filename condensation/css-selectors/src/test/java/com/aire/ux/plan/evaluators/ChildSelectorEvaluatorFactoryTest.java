package com.aire.ux.plan.evaluators;

import static com.aire.ux.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.test.Node;
import lombok.val;
import org.junit.jupiter.api.Test;

public class ChildSelectorEvaluatorFactoryTest extends EvaluatorFactoryTestCase {

  Node node;

  @Test
  void ensureChildSelectorWorks() {
    node = node("div").attribute("child", "false").child(node("div").attribute("child", "true"));
    val plan = parser.parse("div > div").plan(context);
    val eval = plan.evaluate(node, Node.getAdapter());
    assertEquals(1, eval.size());
    assertEquals("true", at(eval, 0).getAttribute("child"));
  }

  @Test
  void ensureComplexSelectorWorks() {
    node =
        node("hello")
            .child(
                node("world")
                    .children(
                        node("drapper").attribute("class", "frapper"),
                        node("drapper").attribute("class", "napper")));
    val plan = parser.parse("world > drapper.frapper").plan(context);
    val result = plan.evaluate(node, Node.getAdapter());
    assertEquals(1, result.size());
    val next = at(result, 0);
    assertEquals("frapper", next.getAttribute("class"));
  }

  @Test
  void ensureChildSelectorDoesNotSelectParentSiblingOtherwiseMatching() {
    node =
        node("html")
            .children(
                node("body")
                    .children(
                        node("div")
                            .attribute("class", "match")
                            .attribute("data-type", "parent-sibling"),
                        node("div")
                            .attribute("class", "porglebee")
                            .attribute("data-type", "parent-sibling")
                            .children(
                                node("span").child(node("div").attribute("class", "match")))));
    val result =
        parser
            .parse("div.porglebee > span > div.match")
            .plan(context)
            .evaluate(node, Node.getAdapter());
    assertEquals(1, result.size());
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return new ChildSelectorCombinatorEvaluatorFactory();
  }
}
