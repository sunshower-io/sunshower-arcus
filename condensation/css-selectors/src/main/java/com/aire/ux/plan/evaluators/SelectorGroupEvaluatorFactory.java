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

public class SelectorGroupEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(ElementSymbol.SelectorGroup);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new SelectorGroupEvaluator(node, context);
  }

  public static class SelectorGroupEvaluator implements Evaluator {

    public SelectorGroupEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {}

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> tree, NodeAdapter<T> hom) {
      return tree;
    }

    @Override
    public String toString() {
      return "<group: select working set.  Cost: 0>";
    }
  }
}
