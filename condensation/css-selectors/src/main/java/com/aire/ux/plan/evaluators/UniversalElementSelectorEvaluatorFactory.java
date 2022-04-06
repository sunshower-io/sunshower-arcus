package com.aire.ux.plan.evaluators;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.select.css.CssSelectorParser.ElementSymbol;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Collections;
import java.util.Set;

public class UniversalElementSelectorEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(ElementSymbol.UniversalSelector);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new UniversalElementSelectorEvaluator(node, context);
  }

  @Override
  public String toString() {
    return getEvaluationTargets().toString();
  }

  private static class UniversalElementSelectorEvaluator
      extends AbstractHierarchySearchingEvaluator {

    private UniversalElementSelectorEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      super(node, context);
    }

    @Override
    protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
      return super.appliesTo(hom, n, workingSet);
    }

    @Override
    public String toString() {
      return "<universal: select descendant working set.  Cost: N>";
    }
  }
}
