package com.aire.ux.select.scenarios.type;

import com.aire.ux.select.css.CssSelectorParser.ElementSymbol;
import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
import lombok.val;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class CssTypeSelectorTest extends TestCase {

  @ParameterizedTest
  @ValueSource(strings = {"a,b", "hello,world", "a, b", "a,     b"})
  @DisplayName("we must be able to parse a simple unioned combinator")
  void ensureSimpleSingleTypeSelectorWorks(String value) {
    val parse = parser.parse(value);
    expectSymbolCount(parse.getSyntaxTree(), ElementSymbol.Union, 1);
    expectSymbolCount(parse.getSyntaxTree(), ElementSymbol.TypeSelector, 2);
  }

  @ParameterizedTest
  @ValueSource(strings = {"hello > world", "hello>world", "a>b", "a+b", "a b", "a ~ b", "a ~   b"})
  void ensureSimpleCombinatorWorks(String value) {
    val parse = parser.parse(value);
    expectNodePropertyCount(parse.getSyntaxTree(), TestCase::isCombinator, 1);
    expectSymbolCount(parse.getSyntaxTree(), ElementSymbol.TypeSelector, 2);
  }

  @ParameterizedTest
  @ValueSource(strings = {"*.b", ".b"})
  void ensureSimpleSelectorSequenceWorks(String value) {
    val parse = parser.parse(value);
    expectSymbolCount(parse.getSyntaxTree(), ElementSymbol.ClassSelector, 1);
  }

  @Test
  void ensureComplexSelectorStatementWorks() {
    val expr = "*.a.b.c b > c ~ d + e f .g #h";
    val t = parser.parse(expr).getSyntaxTree();
    expectSymbolCount(t, ElementSymbol.ClassSelector, 4);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {
        "[href]",
        "a[hello=\"world\"][cool~=beans][sup='people'][attribute] > cool.bean.whatever#world ~ whatever#sup[hello] ~ a[href$=_blank--] + b.c.e.f.g h h i j k"
      })
  void ensureAttributeSelectorWorks(String v) {
    val parse = parser.parse(v);
    System.out.println(parse.getSyntaxTree());
  }
}
