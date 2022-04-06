package com.aire.ux.select.scenarios.type;

import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class AttributeSelectorTest extends TestCase {

  @ParameterizedTest
  @ValueSource(strings = {"a[href]", "[href]", "[href*=coolbeans][bean=beanbeans]"})
  void ensureAttributeGroupIsGenerated(String value) {
    val result = parser.parse(value);
    System.out.println(result.getSyntaxTree());
  }
}
