package com.aire.ux.plan.evaluators;

import static java.lang.String.format;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.select.css.CssSelectorParser.ElementSymbol;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Set;

public class TypeSelectorEvaluatorFactory implements EvaluatorFactory {

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Set.of(ElementSymbol.TypeSelector);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new TypeSelectorEvaluator(node, context);
  }

  public static class TypeSelectorEvaluator extends AbstractHierarchySearchingEvaluator {

    public TypeSelectorEvaluator(SyntaxNode<Symbol, Token> syntaxNode, PlanContext context) {
      super(syntaxNode, context);
    }

    @Override
    protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
      return super.appliesTo(hom, n, workingSet)
          && hom.getType(n).equals(node.getSource().getLexeme());
    }

    public String toString() {
      return format("<type: selecting %s.  Cost: N>", node.getSource().getLexeme());
    }
  }
}
