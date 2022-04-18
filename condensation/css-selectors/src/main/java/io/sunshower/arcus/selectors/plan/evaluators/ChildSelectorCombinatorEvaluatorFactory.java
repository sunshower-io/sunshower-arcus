package io.sunshower.arcus.selectors.plan.evaluators;

import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.selectors.css.CssSelectorParser.ElementSymbol;
import io.sunshower.arcus.selectors.plan.Evaluator;
import io.sunshower.arcus.selectors.plan.EvaluatorFactory;
import io.sunshower.arcus.selectors.plan.PlanContext;
import io.sunshower.arcus.selectors.plan.WorkingSet;
import io.sunshower.arcus.selectors.test.NodeAdapter;
import java.util.Collections;
import java.util.Set;
import lombok.val;

public class ChildSelectorCombinatorEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(ElementSymbol.ChildSelector);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new ChildSelectorCombinatorEvaluator(node, context);
  }

  private static class ChildSelectorCombinatorEvaluator implements Evaluator {

    public ChildSelectorCombinatorEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {}

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
      val result = WorkingSet.<T>create();
      for (val child : workingSet) {
        result.addAll(hom.getChildren(child));
      }
      return result;
    }

    @Override
    public String toString() {
      return ">";
    }
  }
}
