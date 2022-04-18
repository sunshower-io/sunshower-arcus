package io.sunshower.arcus.selectors.select.scenarios.type;

import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.selectors.CssSelectorParserTest.TestCase;
import io.sunshower.arcus.selectors.css.CssSelectorParser.ElementSymbol;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PseudoElementTest extends TestCase {

  @ParameterizedTest
  @ValueSource(strings = {"::hello", "::cool-beans", "::-cool-beans"})
  void ensurePseudoClassWorks(String value) {
    val result = parser.parse(value);
    expectSymbolCount(result.getSyntaxTree(), ElementSymbol.PseudoClass, 1);
    expectSymbolCount(result.getSyntaxTree(), Symbol.symbol(value.substring(2)), 1);
  }

  @ParameterizedTest
  @ValueSource(strings = {":hello", ":cool-beans", ":-cool-beans"})
  void ensurePseudoElementWorks(String value) {
    val result = parser.parse(value);
    expectSymbolCount(result.getSyntaxTree(), ElementSymbol.PseudoElement, 1);
    expectSymbolCount(result.getSyntaxTree(), Symbol.symbol(value.substring(1)), 1);
  }
}
