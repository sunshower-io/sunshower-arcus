package io.sunshower.arcus.selectors.plan.evaluators;

import io.sunshower.arcus.selectors.css.CssSelectorParser.ElementSymbol;

public class PseudoElementEvaluatorFactory extends NoOpEvaluatorFactory {

  public PseudoElementEvaluatorFactory() {
    super(ElementSymbol.PseudoElement);
  }
}
