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
import java.util.List;
import java.util.Set;
import lombok.val;

public class SelectorUnionEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(ElementSymbol.Union);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new UnionEvaluator(node, context);
  }

  static class UnionEvaluator implements Evaluator {

    private final PlanContext context;
    private final List<SyntaxNode<Symbol, Token>> groups;

    public UnionEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      this.context = context;
      this.groups = node.clearChildren();
    }

    @Override
    public String toString() {
      val result = new StringBuilder();
      for (val c : groups) {
        result.append(c).append("\n");
      }
      return result.toString();
    }

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
      val results = WorkingSet.<T>create();
      for (val child : groups) {
        if (child.getSymbol() != ElementSymbol.SelectorGroup) {
          throw new IllegalArgumentException(format("Expected <group>, got %s", child.getSymbol()));
        }
        for (val item : workingSet) {
          results.addAll(context.create(child).plan(context).evaluate(item, hom));
        }
      }
      return results;
    }
  }
}
