package com.aire.ux.plan.evaluators;

import static com.aire.ux.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.test.Node;
import lombok.val;
import org.junit.jupiter.api.Test;

public class DescendantSelectorEvaluatorFactoryTest extends EvaluatorFactoryTestCase {

  Node node;

  @Test
  void ensureDescendantSelectorSelectsAllDescendantsFromRoot() {
    node =
        node("html")
            .children(
                node("span").attribute("not", "selected"),
                node("body")
                    .children(
                        node("div").attribute("class", "whatever").children(node("span")),
                        node("section").children(node("span"))));

    val result = parser.parse("html > body span").plan(context).evaluate(node, Node.getAdapter());
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(node -> "span".equals(node.getType())));
    assertTrue(result.stream().noneMatch(node -> node.hasAttribute("not")));
  }

  @Test
  void ensureNestedDescendantSelectorWorks() {

    node =
        node("html")
            .children(
                node("span").attribute("not", "selected"),
                node("body")
                    .children(
                        node("div").attribute("class", "whatever").children(node("span")),
                        node("section")
                            .children(
                                node("span")
                                    .children(
                                        node("h1").attribute("class", "child"),
                                        node("h2")
                                            .attribute("class", "child")
                                            .child(node("span").attribute("class", "child"))))));

    val result =
        parser.parse("html > body span .child").plan(context).evaluate(node, Node.getAdapter());
    assertTrue(result.stream().allMatch(t -> "child".equals(t.getAttribute("class"))));
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return new DescendantSelectorEvaluatorFactory();
  }
}
