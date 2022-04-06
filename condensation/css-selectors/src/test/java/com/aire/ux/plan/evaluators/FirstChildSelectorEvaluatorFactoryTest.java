package com.aire.ux.plan.evaluators;

import static org.junit.jupiter.api.Assertions.*;

import com.aire.ux.select.ScenarioTestCase;
import lombok.val;
import org.junit.jupiter.api.Test;

class FirstChildSelectorEvaluatorFactoryTest extends ScenarioTestCase {

  @Test
  void ensureFirstChildWorks() {
    val node =
        parseString("<html>\n" + "  <head></head>\n" + "  <body>\n" + "  </body>\n" + "</html>\n");

    val result = eval(":first-child", node);
    assertEquals(1, result.size());
    assertContainsTypes(result, "head");
  }
}
