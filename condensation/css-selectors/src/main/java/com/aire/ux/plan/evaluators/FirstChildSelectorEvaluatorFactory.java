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
import lombok.val;

public class FirstChildSelectorEvaluatorFactory implements EvaluatorFactory {

  static final Symbol symbol = Symbol.symbol("first-child");

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(symbol);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new FirstChildEvaluator(node, context);
  }

  private static final class FirstChildEvaluator extends AbstractHierarchySearchingEvaluator {

    FirstChildEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      super(node, context);
    }

    @Override
    protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
      val parent = hom.getParent(n);
      if (parent == null) {
        return false;
      }
      val children = hom.getChildren(parent);
      return super.appliesTo(hom, n, workingSet) && children.indexOf(n) == 0;
    }
  }
}
