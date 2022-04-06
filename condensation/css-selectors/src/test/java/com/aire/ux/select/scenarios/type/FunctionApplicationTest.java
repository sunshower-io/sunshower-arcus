package com.aire.ux.select.scenarios.type;

import static org.junit.jupiter.api.Assertions.fail;

import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
import java.util.List;
import java.util.Set;
import lombok.val;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class FunctionApplicationTest extends TestCase {

  static MethodDescriptor md(String expr, String... methods) {
    return new MethodDescriptor(expr, methods);
  }

  static List<MethodDescriptor> ensureSimpleFunctionApplicationWorks_factory() {
    return List.of(md("p:nth-last-of-type(2)", "nth-last-of-type"), md(":hello(2)", "hello"));
  }

  static List<MethodDescriptor> ensureMultipleFunctionApplicationWorks_factory() {
    return List.of(md("p:nth-last-of-type(2):hello(2em)", "nth-last-of-type", "hello"));
  }

  @ParameterizedTest
  @MethodSource("ensureSimpleFunctionApplicationWorks_factory")
  void ensureSimpleFunctionApplicationWorks(MethodDescriptor descriptor) {
    val t = parser.parse(descriptor.expression);
    expectNodePropertyCount(t.getSyntaxTree(), u -> has(descriptor, u.getSymbol().name()), 1);
  }

  @ParameterizedTest
  @MethodSource("ensureMultipleFunctionApplicationWorks_factory")
  void ensureMultipleFunctionApplicationWorks(MethodDescriptor descriptor) {
    val t = parser.parse(descriptor.expression);
    expectNodePropertyCount(t.getSyntaxTree(), u -> has(descriptor, u.getSymbol().name()), 2);
  }

  @ParameterizedTest
  @ValueSource(strings = {":not(", ".hello > > world", "p:not(sup *", "hello[t=\"]"})
  void ensureErrorsWork(String value) {
    try {
      val t = parser.parse(value);
      System.out.println(t.getSyntaxTree());
      fail("Expected exception");
    } catch (IllegalArgumentException ex) {
      ex.printStackTrace();
    }
  }

  boolean has(MethodDescriptor descriptor, String... methods) {
    return Set.of(descriptor.methods).containsAll(Set.of(methods));
  }

  static class MethodDescriptor {

    private final String[] methods;
    private final String expression;

    MethodDescriptor(String expression, String... methods) {
      this.methods = methods;
      this.expression = expression;
    }
  }
}
