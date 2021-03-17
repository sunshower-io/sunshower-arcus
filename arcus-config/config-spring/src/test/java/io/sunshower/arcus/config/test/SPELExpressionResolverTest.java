package io.sunshower.arcus.config.test;

import static org.junit.jupiter.api.Assertions.*;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SPELExpressionResolverTest {

  private ExpressionResolver resolver;

  @BeforeEach
  void setUp() {
    resolver = new SPELExpressionResolver();
  }

  @Test
  void ensureExpressionResolverCanResolveExpression() {
    val result = resolver.resolve("Tests.projectDirectory()");
    //    assertEquals(result, Tests.projectDirectory());

  }
}
