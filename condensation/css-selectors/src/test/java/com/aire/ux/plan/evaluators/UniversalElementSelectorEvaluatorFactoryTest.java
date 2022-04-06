package com.aire.ux.plan.evaluators;

import static com.aire.ux.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.test.Node;
import lombok.val;
import org.junit.jupiter.api.Test;

class UniversalElementSelectorEvaluatorFactoryTest extends EvaluatorFactoryTestCase {

  Node node;

  @Test
  void ensureUniversalSelectorSelectsEverything() {
    node = node("html").children(node("body").children(node("section").children(node("h1"))));
    val result = eval("*", node, Node.getAdapter());
    assertEquals(4, result.size());
  }

  @Test
  void ensureUniversalSelectorBeneathCombinatorWorks() {
    node = node("html").children(node("body").children(node("section").children(node("h1"))));
    val result = eval("section > *", node, Node.getAdapter());
    assertEquals(1, result.size());
    assertEquals("h1", at(result, 0).getType());
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return new UniversalElementSelectorEvaluatorFactory();
  }
}
