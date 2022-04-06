package com.aire.ux.plan.evaluators;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.test.NodeAdapter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.ast.AbstractSyntaxTree;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Collections;
import java.util.Set;

@SuppressFBWarnings
public class RootNodeEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.singleton(AbstractSyntaxTree.ROOT_SYMBOL);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new RootNodeEvaluator(node, context);
  }

  public static class RootNodeEvaluator implements Evaluator {

    private final PlanContext context;
    private final SyntaxNode<Symbol, Token> node;

    public RootNodeEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      this.node = node;
      this.context = context;
    }

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> tree, NodeAdapter<T> hom) {
      return tree;
    }

    @Override
    public String toString() {
      return "<root: select working set.  Cost: 0>";
    }
  }
}
