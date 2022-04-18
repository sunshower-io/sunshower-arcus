package io.sunshower.arcus.selectors.plan.evaluators;

import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.selectors.css.CssSelectorParser.ElementSymbol;
import io.sunshower.arcus.selectors.plan.Evaluator;
import io.sunshower.arcus.selectors.plan.EvaluatorFactory;
import io.sunshower.arcus.selectors.plan.PlanContext;
import java.util.Collections;
import java.util.Set;

public class FunctionApplicationEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(ElementSymbol.FunctionApplication);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return null;
  }
}
