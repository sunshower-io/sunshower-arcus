package com.aire.ux.plan.evaluators;

import static com.aire.ux.select.ScenarioTestCase.parseString;
import static com.aire.ux.test.Nodes.node;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aire.ux.plan.EvaluatorFactory;
import lombok.val;
import org.junit.jupiter.api.Test;

class NthChildSelectorEvaluatorFactoryTest extends EvaluatorFactoryTestCase {

  @Test
  void ensureSimpleFirstChildSelectorWorks() {
    val node =
        node("ul")
            .children(
                node("li").attribute("a", "1"),
                node("li").attribute("a", "2"),
                node("li").attribute("a", "3"));

    val result = eval(":nth-child(1n+1)", node);
    assertEquals(3, result.size());
  }

  @Test
  void ensureNthChildSelectorWorksForOddValues() {
    val node =
        node("ul")
            .children(
                node("li").attribute("a", "1"),
                node("li").attribute("a", "2"),
                node("li").attribute("a", "3"));

    val result = eval(":nth-child(2n+1)", node);
    assertEquals(2, result.size());
  }

  @Test
  void ensureCompositeSelectionWorks() {
    val node =
        node("ul")
            .children(
                node("li").attribute("a", "1"),
                node("li").attribute("a", "2"),
                node("li").attribute("a", "3"),
                node("li").attribute("a", "4"),
                node("li").attribute("a", "5"),
                node("li").attribute("a", "6"),
                node("li").attribute("a", "7"),
                node("li").attribute("a", "8"),
                node("li").attribute("a", "9").child(node("hello")),
                node("li").attribute("a", "10"),
                node("li").attribute("a", "11"),
                node("li").attribute("a", "12"),
                node("li").attribute("a", "13"),
                node("li").attribute("a", "14"),
                node("li").attribute("a", "15"),
                node("li").attribute("a", "16"),
                node("li").attribute("a", "17"),
                node("li").attribute("a", "18"),
                node("li").attribute("a", "19"),
                node("li").attribute("a", "20"),
                node("li").attribute("a", "21"),
                node("li").attribute("a", "22"),
                node("li").attribute("a", "23"),
                node("li").attribute("a", "24"));

    val result = eval(":nth-child(n+8):nth-child(-n + 15):nth-child(odd)", node);
    assertEquals(4, result.size());
  }

  @Test
  void ensureNthOfTypeWorks() {
    var doc =
        parseString(
            "<div>\n"
                + "  <div>This element isn't counted.</div>\n"
                + "  <p>1st paragraph.</p>\n"
                + "  <p class=\"fancy\">2nd paragraph.</p>\n"
                + "  <div>This element isn't counted.</div>\n"
                + "  <p class=\"fancy\">3rd paragraph.</p>\n"
                + "  <p>4th paragraph.</p>\n"
                + "</div>\n");
    val docs = eval("p:nth-of-type(2n+1)", doc);
    assertEquals(2, docs.size());
    assertTrue(docs.stream().allMatch(t -> "p".equals(t.getType())));
  }

  @Test
  void ensureChildSelectorWorksAfterSequence() {

    val node =
        node("ul")
            .children(
                node("li").attribute("a", "1"),
                node("li").attribute("a", "2"),
                node("li").attribute("a", "3"),
                node("li").attribute("a", "4"),
                node("li").attribute("a", "5"),
                node("li").attribute("a", "6"),
                node("li").attribute("a", "7"),
                node("li").attribute("a", "8"),
                node("li").attribute("a", "9"),
                node("li").attribute("a", "10"),
                node("li").attribute("a", "11"),
                node("li").attribute("a", "12"),
                node("li").attribute("a", "13"),
                node("li").attribute("a", "14"),
                node("li")
                    .attribute("a", "15")
                    .child(node("hello").attribute("class", "world beans sup")),
                node("li").attribute("a", "16"),
                node("li").attribute("a", "17"),
                node("li").attribute("a", "18"),
                node("li").attribute("a", "19"),
                node("li").attribute("a", "20"),
                node("li").attribute("a", "21"),
                node("li").attribute("a", "22"),
                node("li").attribute("a", "23"),
                node("li").attribute("a", "24"));

    val results =
        eval(":nth-child(n+8):nth-child(-n + 15):nth-child(odd) > hello.world", node).results();
    assertEquals(1, results.size());
  }

  @Test
  void ensureNegativeIndexesWork() {
    val node =
        node("ul")
            .children(
                node("li").attribute("a", "1"),
                node("li").attribute("a", "2"),
                node("li").attribute("a", "3"));

    val result = eval(":nth-child(-n+3)", node);
    assertEquals(3, result.size());
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return new NthChildSelectorEvaluatorFactory();
  }
}
