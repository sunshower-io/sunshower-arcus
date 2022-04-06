package com.aire.ux.plan.evaluators;

import static com.aire.ux.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.*;

import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.test.Node;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class GeneralSiblingEvaluatorFactoryTest extends EvaluatorFactoryTestCase {

  private Node node;

  @ParameterizedTest
  @ValueSource(strings = {"p ~ span", "p~ span", "p~span", "p ~span"})
  void ensureSelectingSimpleMatcherWorks(String selector) {
    node =
        node("ul")
            .children(
                node("span").content("not red"),
                node("p"),
                node("code"),
                node("span").content("red"),
                node("span").content("red"),
                node("code"),
                node("whatever"),
                node("span").content("red"));
    val result = eval("p ~ span", node, Node.getAdapter());
    assertEquals(3, result.size());
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return new GeneralSiblingEvaluatorFactory();
  }
}
