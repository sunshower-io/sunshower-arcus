package com.aire.ux.select.scenarios.type;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.select.ScenarioTestCase;
import com.aire.ux.test.Node;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ComplexScenariosCleanedTest extends ScenarioTestCase {

  private static Node document;

  @BeforeAll
  static void setUpScenario() {
    document = parse("scenarios/complex2.html");
  }

  @Test
  void ensureSelectionsWork() {
    val results = eval("a.w3-bar-item", document);
    assertEquals(184, results.size());
  }

  @Test
  void ensureComplexSelectorWorks() {
    val results = eval("html > body :nth-child(n+5) ~ a", document);
    assertEquals(310, results.size());
  }

  @Test
  void ensureComplexSelectorWorks2() {
    val results = eval("html > body :nth-child(n+5) > * *:nth-child(-2n+1):not(b)", document);
    assertEquals(86, results.size());
  }
}
