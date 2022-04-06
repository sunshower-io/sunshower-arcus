package com.aire.ux.plan.evaluators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aire.ux.select.ScenarioTestCase;
import lombok.val;
import org.junit.jupiter.api.Test;

class NegationSelectorEvaluatorFactoryTest extends ScenarioTestCase {

  @Test
  void ensureSimpleTypeNegationSelectsCorrectNodes() {
    val node = parseString("<html>\n" + "  <body></body>\n" + "</html>\n");

    val result = eval(":not(body)", node);
    assertEquals(1, result.size());
    assertContainsTypes(result, "html");
  }

  @Test
  void ensureSimpleTypeNegationSelectsCorrectNodes2() {
    val node = parseString("<html>\n" + "  <body></body>\n" + "</html>\n");

    val result = eval(":not(html)", node);
    assertEquals(1, result.size());
    assertContainsTypes(result, "body");
  }

  @Test
  void ensureSelectingNegationOnClassWorks() {

    val node = parseString("<html>\n" + "  <body class=\"coolbeans\"></body>\n" + "</html>\n");

    val result = eval(":not(.coolbeans)", node);
    assertEquals(1, result.size());
    assertContainsTypes(result, "html");
  }

  @Test
  void ensureSelectingNegationOnClassWorks2() {

    val node = parseString("<html class=\"coolbeans\">\n" + "  <body></body>\n" + "</html>\n");

    val result = eval(":not(.coolbeans)", node);
    assertEquals(1, result.size());
    assertContainsTypes(result, "body");
  }

  @Test
  void ensureConjunctionWorks() {

    val node =
        parseString(
            "<html class=\"coolbeans\">\n"
                + "  <body>\n"
                + "    <ul>\n"
                + "      <li class=\"first\">\n"
                + "      </li>\n"
                + "      <li class=\"second\">\n"
                + "        <a>one</a>\n"
                + "      </li>\n"
                + "      <li class=\"third\">\n"
                + "        <a>two</a>\n"
                + "      </li>\n"
                + "    </ul>\n"
                + "  </body>\n"
                + "</html>\n");

    val result = eval("li:not(.first):not(li.third)", node);
    assertEquals(1, result.size());
    assertTrue(result.stream().allMatch(t -> t.getType().equals("li")));
  }

  @Test
  void ensureSelectingDeeplyNestedValuesWorks() {

    val node =
        parseString(
            "<html class=\"coolbeans\">\n"
                + "  <body>\n"
                + "    <ul>\n"
                + "      <li class=\"first\">\n"
                + "      </li>\n"
                + "      <li class=\"second\">\n"
                + "        <a>one</a>\n"
                + "      </li>\n"
                + "      <li class=\"third\">\n"
                + "        <a>two</a>\n"
                + "      </li>\n"
                + "    </ul>\n"
                + "  </body>\n"
                + "</html>\n");

    val result = eval("li:not(.first)", node);
    assertEquals(2, result.size());
    assertTrue(result.stream().allMatch(t -> t.getType().equals("li")));
  }
}
