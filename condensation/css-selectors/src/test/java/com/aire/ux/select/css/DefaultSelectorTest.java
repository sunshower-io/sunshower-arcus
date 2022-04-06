package com.aire.ux.select.css;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.plan.DefaultPlanContext;
import com.aire.ux.plan.evaluators.RootNodeEvaluatorFactory.RootNodeEvaluator;
import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
import com.aire.ux.test.Node;
import com.aire.ux.test.Nodes;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DefaultSelectorTest extends TestCase {

  private Selector selector;
  private DefaultPlanContext context;

  @BeforeEach
  protected void setUp() {
    super.setUp();
    context = new DefaultPlanContext();
  }

  //  @DisplayName("ensure positive A*n+B works")
  @ParameterizedTest
  @ValueSource(
      strings = {
        ":nth-child(2n + 4)",
        "tr:nth-child(odd)",
        "tr:nth-child(2n + 1)",
        "tr:nth-child(2n+ 1)",
        "tr:nth-child(2 n + 1)",
        "tr:nth-child(even)",
        "tr:nth-child(odd)",
        ":nth-child(7)",
        ":nth-child(5n)",
        "p:nth-child(-n+15)",
        ":nth-child(- 4 n + 15)"
      })
  void ensurePositiveANPlusBWorks(String value) {
    val r = parser.parse(value);
    System.out.println(r.getSyntaxTree());
  }

  @Test
  void ensureEvenWorks() {
    val result = parser.parse("div:nth-child(even)");
    expectNodePropertyCount(
        result.getSyntaxTree(),
        (node) -> node.getSource() != null && node.getSource().getLexeme().equals("even"),
        1);
  }

  @Test
  void ensureOddWorks() {
    val result = parser.parse("div:nth-child(odd)");
    expectNodePropertyCount(
        result.getSyntaxTree(),
        (node) -> node.getSource() != null && node.getSource().getLexeme().equals("odd"),
        1);
  }

  @Test
  void ensurePlanToStringIsHelpful() {
    val result =
        parser.parse(
            "span > span[sup $=beans]:nth-child(1) > span.test:nth-child(1).test:nth-child(1).test");

    val s = result.plan(context).toString();
    System.out.println(s);
  }

  @Test
  void ensureCollectingSimpleTypeSelectorWorks() {
    val plan = parser.parse("hello").plan(context);
    val evals = plan.getEvaluators(RootNodeEvaluator.class);
    assertEquals(1, evals.size());
  }

  @Test
  void ensureSelectorCanRetrieveNodesAtFirstLevel() {
    val plan = parser.parse("hello").plan(context);
    val tree = Nodes.node("hello");
    val results = plan.evaluate(tree, Node.getAdapter());
    //    assertEquals(1, results.size());
  }

  @Test
  void ensureCollectingSimpleTypeSelectorDescendantCombinatorClassSelectorWorks() {
    val plan = parser.parse("hello > .world").plan(context);
    val tree = Nodes.node("hello").child(Nodes.node("world").attribute("class", "world"));
  }
}
