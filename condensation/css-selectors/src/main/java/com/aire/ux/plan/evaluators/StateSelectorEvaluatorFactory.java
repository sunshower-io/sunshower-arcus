package com.aire.ux.plan.evaluators;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.test.NodeAdapter;
import com.aire.ux.test.NodeAdapter.State;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

public class StateSelectorEvaluatorFactory implements EvaluatorFactory {

  private final Set<Symbol> symbols;

  public StateSelectorEvaluatorFactory(State... states) {
    this.symbols = Arrays.stream(states).map(t -> t.toSymbol()).collect(Collectors.toSet());
  }

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Collections.emptySet();
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new StateSelectorEvaluator(node, context);
  }

  static final class StateSelectorEvaluator extends AbstractHierarchySearchingEvaluator {

    final String state;

    StateSelectorEvaluator(@Nonnull SyntaxNode<Symbol, Token> node, @Nonnull PlanContext context) {
      super(node, context);
      this.state = node.getSource().getLexeme();
    }

    @Override
    protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
      return super.appliesTo(hom, n, workingSet) && hom.hasState(n, hom.stateFor(state));
    }
  }
}
