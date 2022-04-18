package io.sunshower.arcus.selectors.plan.evaluators;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.selectors.ScenarioTestCase;
import lombok.val;
import org.junit.jupiter.api.Test;

class LastChildEvaluatorFactoryTest extends ScenarioTestCase {

  @Test
  void ensureFirstChildWorks() {
    val node =
        parseString("<html>\n" + "  <head></head>\n" + "  <body>\n" + "  </body>\n" + "</html>\n");

    val result = eval(":last-child", node);
    assertEquals(1, result.size());
    assertContainsTypes(result, "body");
  }
}
