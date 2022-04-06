package com.aire.ux.select.scenarios.type;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class W3ExamplesScenarioTest extends TestCase {

  @Test
  void ensureParsingSimpleCombinatorWorks() {
    val expr = "UL LI";
    val tree = parser.parse(expr);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "*",
        "LI",
        "UL LI",
        "UL OL+LI",
        "H1 + *[REL=up]",
        "UL OL LI.red",
        "LI.red.level",
        "#x34y"
      })
  void ensureSelectorSpecificityExamplesWork(String selector) {
    val r = parser.parse(selector);
    System.out.println(r.getSyntaxTree());
    assertNotNull(r);
    assertTrue(r.getSyntaxTree().reduce(0, (node, n) -> n + 1) > 0);
  }
}
