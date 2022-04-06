package com.aire.ux.plan.evaluators;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Collections;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.val;

public class LastChildSelectorEvaluatorFactory implements EvaluatorFactory {

  static final Symbol symbol = Symbol.symbol("last-child");

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(symbol);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new LastChildEvaluator(node, context);
  }

  static final class LastChildEvaluator extends AbstractHierarchySearchingEvaluator {

    LastChildEvaluator(@Nonnull SyntaxNode<Symbol, Token> node, @Nonnull PlanContext context) {
      super(node, context);
    }

    @Override
    protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
      val parent = hom.getParent(n);
      if (parent == null) {
        return false;
      }
      val children = hom.getChildren(parent);
      return super.appliesTo(hom, n, workingSet) && children.lastIndexOf(n) == children.size() - 1;
    }
  }
}
