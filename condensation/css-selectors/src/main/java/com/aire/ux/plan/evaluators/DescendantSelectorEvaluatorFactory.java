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
import java.util.LinkedList;
import java.util.Set;
import lombok.val;

public class DescendantSelectorEvaluatorFactory implements EvaluatorFactory {

  public DescendantSelectorEvaluatorFactory() {}

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(ElementSymbol.DescendantSelector);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new DescendantSelectorEvaluator();
  }

  static final class DescendantSelectorEvaluator implements Evaluator {

    @Override
    public String toString() {
      return "[descendant]";
    }

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
      val result = WorkingSet.<T>create();
      for (val node : workingSet) {
        val stack = new LinkedList<T>(hom.getChildren(node));
        while (!stack.isEmpty()) {
          val iter = stack.listIterator();
          while (iter.hasNext()) {
            val next = iter.next();
            result.add(next);
            iter.remove();
            for (val e : hom.getChildren(next)) {
              iter.add(e);
            }
          }
        }
      }
      return result;
    }
  }
}
