package io.sunshower.arcus.selectors.plan.evaluators;

import io.sunshower.arcus.ast.AbstractSyntaxTree;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.selectors.css.CssSelectorParser.ElementSymbol;
import io.sunshower.arcus.selectors.plan.Evaluator;
import io.sunshower.arcus.selectors.plan.EvaluatorFactory;
import io.sunshower.arcus.selectors.plan.Plan;
import io.sunshower.arcus.selectors.plan.PlanContext;
import io.sunshower.arcus.selectors.plan.WorkingSet;
import io.sunshower.arcus.selectors.test.NodeAdapter;
import java.util.Collections;
import java.util.Set;
import java.util.Stack;
import lombok.val;

public class NegationSelectorEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(ElementSymbol.Negation);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new NegationSelectorEvaluator(node, context);
  }

  private static class NegationSelectorEvaluator implements Evaluator {

    private final Plan plan;
    private final PlanContext context;
    private final AbstractSyntaxTree<Symbol, Token> subroot;

    public NegationSelectorEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      this.context = context;
      this.subroot = new AbstractSyntaxTree<Symbol, Token>(node.clearChildren());
      this.plan = context.create(subroot.getRoot()).plan(context);
    }

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
      val current = WorkingSet.withExclusions(workingSet);
      current.addAll(workingSet.results());
      val exclusions = plan.evaluate(current, hom);
      val stack = new Stack<T>();
      stack.addAll(current.results());
      val results = WorkingSet.<T>create();
      while (!stack.isEmpty()) {
        val next = stack.pop();
        if ((exclusions.isExcluded(next) || !exclusions.contains(next))) {
          if (!current.isExcluded(next)) {
            results.add(next);
          }
        }
        stack.addAll(hom.getChildren(next));
      }
      results.excludeAll(workingSet);
      for (val c : exclusions) {
        results.exclude(c);
      }
      return results;
    }
  }
}
