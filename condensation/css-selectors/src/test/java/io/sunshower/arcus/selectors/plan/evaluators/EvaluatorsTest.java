package io.sunshower.arcus.selectors.plan.evaluators;

import static io.sunshower.arcus.selectors.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.selectors.plan.EvaluatorFactory;
import io.sunshower.arcus.selectors.test.Node;
import lombok.val;
import org.junit.jupiter.api.Test;

public class EvaluatorsTest extends EvaluatorFactoryTestCase {


  private Node node;

  /**
   * test
   * {@code
   *
   * <ul>
   *   <li>not selected</li>
   *   <li>
   *     <span>
   *       <h1/>
   *     </span>
   *   </li>
   * </ul>
   *
   * }
   */
  @Test
  void ensureGeneralSelectorWorksUnderAdjacentSiblingSelector() {
    node = node("ul")
        .children(
            node("li").content("not selected"),
            node("li").children(
                node("span")
                .children(node("h1"))
            )
        );

    val results = eval("ul>li~li *", node, Node.getAdapter());
    assertEquals(2, results.size());
    assertEquals("span", at(results, 0).getType());
    assertEquals("h1", at(results, 1).getType());
  }


  @Override
  protected EvaluatorFactory createFactory() {
    return null;
  }
}
