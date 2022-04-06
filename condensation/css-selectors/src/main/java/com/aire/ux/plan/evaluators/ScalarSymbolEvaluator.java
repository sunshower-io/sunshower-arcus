package com.aire.ux.plan.evaluators;

import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;

public class ScalarSymbolEvaluator extends AbstractHierarchySearchingEvaluator {

  final Symbol symbol;

  public ScalarSymbolEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context, Symbol symbol) {
    super(node, context);
    this.symbol = symbol;
  }

  @Override
  protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
    return super.appliesTo(hom, n, workingSet) && hom.hasState(n, hom.stateFor(symbol.name()));
  }
}
