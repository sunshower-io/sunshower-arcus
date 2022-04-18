package io.sunshower.arcus.selectors.plan.evaluators;

import static io.sunshower.arcus.selectors.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sunshower.arcus.selectors.css.CssSelectorParser.ElementSymbol;
import io.sunshower.arcus.selectors.plan.EvaluatorFactory;
import io.sunshower.arcus.selectors.plan.WorkingSet;
import io.sunshower.arcus.selectors.test.Node;
import lombok.val;
import org.junit.jupiter.api.Test;

class TypeSelectorEvaluatorFactoryTest extends EvaluatorFactoryTestCase {

  @Test
  void ensureSelectingSimpleValueFromSimpleTreeWorks() {
    val selector = parser.parse("div");
    val selectors = selector.find(t -> t.getSymbol() == ElementSymbol.TypeSelector);
    assertEquals(1, selectors.size());
    tree = node("body").child(node("div"));
    System.out.println(tree);
    System.out.println(tree.getChildren());
    val result =
        factory.create(selectors.get(0), context).evaluate(WorkingSet.of(tree), Node.getAdapter());
    assertEquals(result.size(), 1);
    assertEquals("div", result.iterator().next().getType());
  }

  @Test
  void ensureSelectingSimpleValueFromPlanWorks() {
    tree = node("body").child(node("div"));
    val selector = parser.parse("div");
    val list = selector.plan(context).evaluate(tree, Node.getAdapter());
    assertEquals(1, list.size());
    assertEquals("div", list.iterator().next().getType());
  }

  @Test
  void ensureSelectingMultipleDivsInHierarchyWorks() {
    tree = node("body").child(node("div").child(node("div")));
    val selector = parser.parse("div");
    val list = selector.plan(context).evaluate(tree, Node.getAdapter());
    assertEquals(2, list.size());
    assertTrue(list.stream().allMatch(t -> t.getType().equals("div")));
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return new TypeSelectorEvaluatorFactory();
  }
}
