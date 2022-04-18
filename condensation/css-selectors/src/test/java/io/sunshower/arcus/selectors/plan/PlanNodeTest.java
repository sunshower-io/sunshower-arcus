package io.sunshower.arcus.selectors.plan;

import io.sunshower.arcus.selectors.CssSelectorParserTest.TestCase;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlanNodeTest extends TestCase {

  @BeforeEach
  protected void setUp() {
    super.setUp();
  }

  @Test
  void ensureCollectingSimpleTypeSelectorWorks() {
    val result = parser.parse("hello");
    //    val plan = result.plan();

  }
}
