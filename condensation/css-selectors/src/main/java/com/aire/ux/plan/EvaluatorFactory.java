package com.aire.ux.plan;

import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Set;

public interface EvaluatorFactory {
  Set<Symbol> getEvaluationTargets();

  Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context);
}
