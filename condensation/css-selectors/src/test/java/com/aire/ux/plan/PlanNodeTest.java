package com.aire.ux.plan;

import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
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
