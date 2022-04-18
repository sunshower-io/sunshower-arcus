package io.sunshower.arcus.selectors.plan.evaluators;

import static java.lang.String.format;

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

public class AdjacentSiblingEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(ElementSymbol.AdjacentSiblingSelector);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new AdjacentSiblingEvaluator(node, context);
  }

  private static class AdjacentSiblingEvaluator implements Evaluator {

    private final CompositeEvaluator delegate;

    public AdjacentSiblingEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      this.delegate = new CompositeEvaluator(context, node.getChildren());
    }

    @Override
    public <T> int computeCost(Set<T> workingSet, NodeAdapter<T> hom) {
      int results = 0;
      for (val element : workingSet) {
        val sibling = hom.getSucceedingSibling(element);
        if (sibling != null) {
          results += delegate.computeCost(Set.of(sibling), hom);
        }
      }
      return results;
    }

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
      val results = WorkingSet.<T>create();
      for (val element : workingSet) {
        val sibling = hom.getSucceedingSibling(element);
        if (sibling != null) {
          results.addAll(delegate.evaluate(WorkingSet.of(sibling), hom));
        }
      }
      return results;
    }

    @Override
    public String toString() {
      return format("+ %s", delegate.toString());
    }
  }
}
