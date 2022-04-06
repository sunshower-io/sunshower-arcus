package com.aire.ux.plan.evaluators;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.val;

/** */
public final class CompositeEvaluator implements Evaluator {

  final PlanContext context;
  final List<Evaluator> evaluators;

  public CompositeEvaluator(
      PlanContext context, Collection<? extends SyntaxNode<Symbol, Token>> tokens) {
    this.context = context;
    this.evaluators =
        tokens.stream()
            .map(token -> context.lookup(token).create(token, context))
            .collect(Collectors.toList());
  }

  @Override
  public <T> int computeCost(Set<T> workingSet, NodeAdapter<T> hom) {
    return evaluators.stream()
        .sequential()
        .reduce(0, (t, u) -> t + u.computeCost(workingSet, hom), (t, u) -> u + t);
  }

  @Override
  public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
    val results = WorkingSet.<T>create();
    for (val evaluator : evaluators) {
      results.addAll(evaluator.evaluate(workingSet, hom));
    }
    return results;
  }

  @Override
  public String toString() {
    return evaluators.stream().map(t -> t.toString()).collect(Collectors.joining(" "));
  }
}
