package io.sunshower.arcus.selectors.plan.evaluators;

import io.sunshower.arcus.selectors.plan.EvaluatorFactory;
import org.junit.jupiter.api.Test;

public class NthChildFunctionTest extends EvaluatorFactoryTestCase {

  @Test
  void ensureNthChildGrammarIsSupported() {
    parser.parse("div:nth-child(2)").plan(context);
  }

  @Override
  protected EvaluatorFactory createFactory() {
    return null;
  }
}
