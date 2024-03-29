package io.sunshower.arcus.selectors.plan.evaluators;

import static java.lang.String.format;

import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.selectors.css.CssSelectorParser.ElementSymbol;
import io.sunshower.arcus.selectors.plan.Evaluator;
import io.sunshower.arcus.selectors.plan.PlanContext;
import io.sunshower.arcus.selectors.plan.WorkingSet;
import io.sunshower.arcus.selectors.test.NodeAdapter;
import lombok.val;

public class ClassSelectorEvaluatorFactory extends AbstractMemoizingEvaluatorFactory {

  public ClassSelectorEvaluatorFactory() {
    super(ElementSymbol.ClassSelector);
  }

  @Override
  protected Evaluator createEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
    val child = node.removeChild(0);
    val classValue = child.getSource().getLexeme();

    return new ClassSelectorEvaluator(node, context, classValue);
  }

  private static class ClassSelectorEvaluator extends AbstractHierarchySearchingEvaluator {

    final String classValue;

    public ClassSelectorEvaluator(
        SyntaxNode<Symbol, Token> node, PlanContext context, String classValue) {
      super(node, context);
      this.classValue = classValue;
    }

    @Override
    protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
      val classes = hom.getAttribute(n, "class");
      if (classes != null) {
        val cls = classes.split("\\s+");
        for (val cl : cls) {
          if (classValue.equals(cl)) {
            return super.appliesTo(hom, n, workingSet);
          }
        }
      }
      return false;
    }

    @Override
    public String toString() {
      return format(".%s", classValue);
    }
  }
}
