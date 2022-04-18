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
