package com.aire.ux.select.scenarios.type;

import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class NegationTest extends TestCase {

  @ParameterizedTest
  @ValueSource(
      strings = {
        ":not(hello)",
        ":not(::first-line)",
        ":not(a[href='google.com'])",
        "a b > c + d ~whatever p:not(article) :not([world*=whatever])",
        "p:not(*.whatever[href='cool.com'][bean*=stuff][single] ~ #whodat:second-child(2rem)):nth-child(2n + 1), hello > world:not(:not(p))"
      })
  void ensureNotWorks(String value) {
    val t = parser.parse(value);
    System.out.println(t.getSyntaxTree());
  }

  @Test
  void ensureNegationOnSimpleArgsWorks() {
    val expr = ":not(hello p > world :not(bean .cool))";
    val t = parser.parse(expr);
    System.out.println(t.getSyntaxTree());
  }
}
