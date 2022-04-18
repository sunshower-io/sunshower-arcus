package io.sunshower.arcus.selectors.plan.evaluators;

import static io.sunshower.arcus.selectors.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.arcus.selectors.plan.EvaluatorFactory;
import io.sunshower.arcus.selectors.test.Node;
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
