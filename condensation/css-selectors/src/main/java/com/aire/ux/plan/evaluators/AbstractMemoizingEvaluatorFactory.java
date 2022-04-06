package com.aire.ux.plan.evaluators;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.plan.PlanContext;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMemoizingEvaluatorFactory implements EvaluatorFactory, AutoCloseable {

  private final Symbol symbol;
  private Map<SyntaxNode<Symbol, Token>, Evaluator> memoizedEvaluators;

  protected AbstractMemoizingEvaluatorFactory(Symbol symbol) {
    this.symbol = symbol;
    this.memoizedEvaluators = new HashMap<>();
  }

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(symbol);
  }

  @Override
  public final Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return memoizedEvaluators.computeIfAbsent(node, t -> createEvaluator(node, context));
  }

  @Override
  public void close() {
    memoizedEvaluators.clear();
  }

  @Override
  public String toString() {
    return getEvaluationTargets().toString();
  }

  protected abstract Evaluator createEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context);
}
