package com.aire.ux.plan.evaluators;

import com.aire.ux.select.css.CssSelectorParser.ElementSymbol;

public class PseudoElementEvaluatorFactory extends NoOpEvaluatorFactory {

  public PseudoElementEvaluatorFactory() {
    super(ElementSymbol.PseudoElement);
  }
}
