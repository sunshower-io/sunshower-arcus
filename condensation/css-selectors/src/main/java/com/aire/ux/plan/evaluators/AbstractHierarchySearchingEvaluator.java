package com.aire.ux.plan.evaluators;

import static java.lang.String.format;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.test.NodeAdapter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Objects;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.val;

@SuppressFBWarnings
public abstract class AbstractHierarchySearchingEvaluator implements Evaluator {

  @Nonnull protected final PlanContext context;
  @Nonnull protected final SyntaxNode<Symbol, Token> node;

  public AbstractHierarchySearchingEvaluator(
      @Nonnull SyntaxNode<Symbol, Token> node, @Nonnull PlanContext context) {
    Objects.requireNonNull(node);
    Objects.requireNonNull(context);
    this.node = node;
    this.context = context;
  }

  @Override
  public <T> int computeCost(Set<T> workingSet, NodeAdapter<T> hom) {
    int i = 0;
    for (val node : workingSet) {
      hom.reduce(node, i, (n, cost) -> cost + 1);
    }
    return i;
  }

  @Override
  public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
    val results = WorkingSet.<T>withExclusions(workingSet);
    for (val node : workingSet) {
      hom.reduce(
          node,
          results,
          (n, rs) -> {
            if (appliesTo(hom, n, workingSet)) {
              rs.add(n);
            } else {
              rs.exclude(n);
            }
            return rs;
          });
    }
    return results;
  }

  protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
    return !workingSet.isExcluded(n);
  }

  @Override
  public String toString() {
    return format("%s[%s]", getClass().getSimpleName(), node.getSymbol());
  }
}
