package com.aire.ux.plan.evaluators;

import static com.aire.ux.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.plan.EvaluatorFactory;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SelectorUnionEvaluatorFactoryTest extends EvaluatorFactoryTestCase {

  @ParameterizedTest
  @ValueSource(strings = {"ul, span > cool, span", "ul,span>cool,span", "ul  , span  > cool, span"})
  void ensureSelectorSelectsSimpleUnion(String expression) {
    val node =
        node("html").children(node("ul"), node("span").children(node("cool")), node("frapper"));
    val results = eval(expression, node);
    assertEquals(3, results.size());
    assertContainsTypes(results, "ul", "span", "cool");
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return new SelectorUnionEvaluatorFactory();
  }
}
