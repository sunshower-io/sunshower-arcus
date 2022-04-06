package com.aire.ux.select.css;

import static com.aire.ux.select.css.CssSelectorToken.ApplicationEnd;
import static com.aire.ux.select.css.CssSelectorToken.AttributeGroup;
import static com.aire.ux.select.css.CssSelectorToken.AttributeGroupEnd;
import static com.aire.ux.select.css.CssSelectorToken.Comma;
import static com.aire.ux.select.css.CssSelectorToken.GreaterThan;
import static com.aire.ux.select.css.CssSelectorToken.Identifier;
import static com.aire.ux.select.css.CssSelectorToken.Not;
import static com.aire.ux.select.css.CssSelectorToken.StrictEqualityOperator;
import static com.aire.ux.select.css.CssSelectorToken.Universal;
import static com.aire.ux.select.css.CssSelectorToken.Whitespace;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.ast.core.Token;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.Test;

class TokenBufferTest {

  @Test
  void ensureDescendantSelectorWorks() {
    val expr = "ancestor descendant";
    expectTokens(expr, Identifier, Whitespace, Identifier);
  }

  @Test
  void ensureChildConsumesWhitespaceCorrectly() {
    val expr = "parent > child";
    expectTokens(expr, Identifier, GreaterThan, Whitespace, Identifier);
  }

  @Test
  void ensureWhitespaceIsChompedCorrectlyForSelectorGroup() {
    val expr = "hello, \t world";
    expectTokens(expr, Identifier, Comma, Whitespace, Identifier);
  }

  @Test
  void ensureComplexOperatorSetWorks() {
    val expr =
        "hello -world- > input:not([type=\"hidden\"]):not([type=\"radio\"]):not([type=\"checkbox\"]) * porglesbees";
    expectTokens(
        expr,
        Identifier,
        Whitespace,
        Identifier,
        GreaterThan,
        Whitespace,
        Identifier,
        Not,
        AttributeGroup,
        Identifier,
        StrictEqualityOperator,
        CssSelectorToken.String,
        AttributeGroupEnd,
        ApplicationEnd,
        Not,
        AttributeGroup,
        Identifier,
        StrictEqualityOperator,
        CssSelectorToken.String,
        AttributeGroupEnd,
        ApplicationEnd,
        Not,
        AttributeGroup,
        Identifier,
        StrictEqualityOperator,
        CssSelectorToken.String,
        AttributeGroupEnd,
        ApplicationEnd,
        Whitespace,
        Universal,
        Whitespace,
        Identifier);
  }

  @Test
  void ensureStringParsingWorks() {
    expectTokens(
        "\"hello world\"  \"how are you?\"",
        CssSelectorToken.String,
        Whitespace,
        CssSelectorToken.String);
  }

  private void expectTokens(String s, CssSelectorToken... tokens) {
    CssSelectorToken.createTokenBuffer().stream(s).forEach(System.out::println);
    val results =
        CssSelectorToken.createTokenBuffer().stream(s)
            .map(Token::getType)
            .collect(Collectors.toList());
    assertEquals(List.of(tokens), results);
  }
}
