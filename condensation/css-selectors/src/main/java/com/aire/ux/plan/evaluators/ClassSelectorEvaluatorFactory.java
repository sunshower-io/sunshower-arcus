package com.aire.ux.plan.evaluators;

import static java.lang.String.format;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.select.css.CssSelectorParser.ElementSymbol;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
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
