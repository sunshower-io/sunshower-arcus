package io.sunshower.arcus.selectors.plan.evaluators;

import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.selectors.plan.PlanContext;
import io.sunshower.arcus.selectors.plan.WorkingSet;
import io.sunshower.arcus.selectors.test.NodeAdapter;

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
