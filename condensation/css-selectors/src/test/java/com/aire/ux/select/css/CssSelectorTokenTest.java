package com.aire.ux.select.css;

import static com.aire.ux.select.css.CssSelectorToken.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CssSelectorTokenTest {

  @ParameterizedTest
  @ValueSource(strings = {"1.0", "1", "2.000", "45"})
  void ensureNumericWorks(String value) {
    expect(value, Numeric, value);
  }

  @ParameterizedTest
  @ValueSource(strings = {"2em", "2rem", "2.9em", "0.1em"})
  void ensureDimensionWorks(String value) {
    expect(value, Dimension, value);
  }

  @Test
  void ensureMinusWorks() {
    expect("-", Minus, "-");
  }

  @ParameterizedTest
  @ValueSource(strings = {":"})
  void ensurePseudoElementMatches(String value) {
    expect(value, PseudoElement, value);
  }

  @ParameterizedTest
  @ValueSource(strings = {"::"})
  void ensurePseudoClassMatches(String value) {
    expect(value, PseudoClass, value);
  }

  @Test
  void ensureElementRegexMatchesSimpleElement() {
    val matcher = Identifier.getPattern().matcher("p");
    assertTrue(matcher.matches());
    val group = matcher.group(Identifier.name());
    assertEquals(group, "p");
  }

  @Test
  void ensureElementRegexMatchesSnakeCase() {
    val matcher = Identifier.getPattern().matcher("p-u");
    assertTrue(matcher.matches());
    val group = matcher.group(Identifier.name());
    assertEquals(group, "p-u");
  }

  @Test
  void ensureElementRegexMatchesLongElementName() {
    val matcher = Identifier.getPattern().matcher("hello-world-coolbeans1");
    assertTrue(matcher.matches());
    val group = matcher.group(Identifier.name());
    assertEquals(group, "hello-world-coolbeans1");
  }

  @Test
  void ensureUniversalSelectorIsMatched() {

    val matcher = CssSelectorToken.Universal.getPattern().matcher("*");
    assertTrue(matcher.matches());
    val group = matcher.group(CssSelectorToken.Universal.name());
    assertEquals(group, "*");
  }

  @Test
  void ensureClassSelectorIsMatched() {
    val matcher = CssSelectorToken.Class.getPattern().matcher(".");
    assertTrue(matcher.matches());
    val group = matcher.group(CssSelectorToken.Class.name());
    assertEquals(group, ".");
  }

  @Test
  void ensureIdMatcherIsSelected() {
    val matcher = CssSelectorToken.IdentifierSelector.getPattern().matcher("#");
    assertTrue(matcher.matches());
    val group = matcher.group(CssSelectorToken.IdentifierSelector.name());
    assertEquals(group, "#");
  }

  @Test
  void ensureIdentifierMatchesIdentifiers() {
    expect("hello", Identifier, "hello");
    expect("-hello", Identifier, "-hello");
    expect("-hello-world", Identifier, "-hello-world");
  }

  @Test
  void ensureUnclosedStringsAreMatched() {
    expect("\"hello", UnclosedString, "\"hello");
    expect("\'hello", UnclosedString, "\'hello");
  }

  @Test
  void ensureNumericPatternMatchesNumbers() {
    expect("0", Numeric, "0");
    expect("0.0", Numeric, "0.0");
  }

  @ParameterizedTest
  @ValueSource(strings = {"'string one'", "\"string ' two\""})
  void ensureStringValuesAreParsedCorrectly(String str) {
    expect(str, CssSelectorToken.String, str);
  }

  private void expect(String expr, CssSelectorToken token, String lexeme) {
    val matcher = token.getPattern().matcher(expr);
    assertTrue(matcher.matches());
    val group = matcher.group(token.name());
    assertEquals(group, lexeme);
  }
}
