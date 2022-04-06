package com.aire.ux.plan.evaluators;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.EvaluatorFactory;
import com.aire.ux.plan.Expression;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.select.css.CssSelectorToken;
import com.aire.ux.test.NodeAdapter;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import lombok.val;

public class NthChildSelectorEvaluatorFactory implements EvaluatorFactory {

  static final Symbol nthChild = Symbol.symbol("nth-child");
  static final Symbol nthOfType = Symbol.symbol("nth-of-type");

  @Override
  public Set<Symbol> getEvaluationTargets() {
    return Set.of(nthChild, nthOfType);
  }

  @Override
  public Evaluator create(SyntaxNode<Symbol, Token> node, PlanContext context) {
    return new NthChildSelectorEvaluator(node, context);
  }

  static final class NthChildSelectorEvaluator implements Evaluator {

    final Evaluator delegate;
    final PlanContext context;
    final SyntaxNode<Symbol, Token> node;

    public NthChildSelectorEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      this.node = node;
      this.context = context;
      this.delegate = detectDelegate(node);
    }

    private static boolean is(SyntaxNode<Symbol, Token> node, String value) {
      val children = node.getChildren();
      if (children.size() == 1) {
        val child = children.get(0);
        return child.getSource().getType() == CssSelectorToken.Identifier
            && value.equalsIgnoreCase(child.getSource().getLexeme());
      }
      return false;
    }

    private static boolean isScalarNumber(SyntaxNode<Symbol, Token> node) {
      val children = node.getChildren();
      return children.size() == 1
          && children.get(0).getSource().getType() == CssSelectorToken.Numeric;
    }

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
      return delegate.evaluate(workingSet, hom);
    }

    final Evaluator detectDelegate(SyntaxNode<Symbol, Token> node) {
      if (isScalarNumber(node)) {
        return new ScalarEvaluator(node, context);
      }
      if (is(node, "odd")) {
        return new OddEvaluator(node, context);
      }
      if (is(node, "even")) {
        return new EvenEvaluator(node, context);
      }
      return new ExpressionEvaluator(node, context);
    }
  }

  static final class ExpressionEvaluator extends NthChildEvaluator {

    final Expression expression;

    ExpressionEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      this.expression = Expression.parse(node.clearChildren());
    }

    @Override
    protected int offset(int idx) {
      return expression.apply(idx) - 1;
    }

    protected <T> boolean is(List<T> siblings, T value, int i) {
      val r = offset(i);
      return r >= 0 && r < siblings.size();
    }
  }

  abstract static class NthChildEvaluator implements Evaluator {

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> workingSet, NodeAdapter<T> hom) {
      val results = WorkingSet.<T>withExclusions(workingSet);
      for (val node : workingSet) {
        hom.reduce(
            node,
            results,
            (n, rs) -> {
              rs.addAll(collectMatching(workingSet, n, hom));
              return rs;
            });
      }
      return results;
    }

    protected abstract int offset(int idx);

    protected abstract <T> boolean is(List<T> child, T value, int index);

    private <T> T get(List<T> sibs, int idx) {
      if (idx >= 0 && idx < sibs.size()) {
        return sibs.get(idx);
      }
      return null;
    }

    private <T> WorkingSet<T> collectMatching(WorkingSet<T> workingSet, T n, NodeAdapter<T> hom) {
      val parent = hom.getParent(n);
      if (parent == null) {
        return WorkingSet.empty();
      }
      val results = WorkingSet.<T>withExclusions(workingSet);

      val siblings = hom.getChildren(parent);
      for (int i = 0; i < siblings.size(); i++) {
        val idx = offset(i);
        val sibling = get(siblings, idx);
        if (sibling != null && is(siblings, sibling, i)) {
          if (!workingSet.isExcluded(sibling)) {
            results.add(sibling);
          }
        }
      }
      for (val sib : siblings) {
        if (!results.contains(sib)) {
          results.exclude(sib);
        }
      }
      return results;
    }
  }

  /**
   * these take advantage of the fact that odd(n + 1) == even(n) and the fact that java collections
   * are zero-indexed while CSS selectors are 1-indexed
   */
  static final class EvenEvaluator extends NthChildEvaluator {

    EvenEvaluator(@Nonnull SyntaxNode<Symbol, Token> node, @Nonnull PlanContext context) {
      node.removeChild(0);
    }

    @Override
    protected int offset(int idx) {
      return idx;
    }

    @Override
    @SuppressFBWarnings
    protected <T> boolean is(List<T> children, T value, int index) {
      return children.indexOf(value) % 2 == 1;
    }
  }

  static final class OddEvaluator extends NthChildEvaluator {

    public OddEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      node.removeChild(0);
    }

    @Override
    protected int offset(int idx) {
      return idx;
    }

    @Override
    protected <T> boolean is(List<T> children, T value, int index) {
      return children.indexOf(value) % 2 == 0;
    }
  }

  static final class ScalarEvaluator extends AbstractHierarchySearchingEvaluator {

    final int offset;

    ScalarEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
      super(node, context);
      this.offset = readOffset();
    }

    private int readOffset() {
      val child = node.removeChild(0);
      return Integer.parseInt(child.getSource().getLexeme());
    }

    @Override
    protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
      val parent = hom.getParent(n);
      if (parent == null) {
        return false;
      }
      val children = hom.getChildren(parent);
      return super.appliesTo(hom, n, workingSet) && children.indexOf(n) + 1 == offset;
    }
  }
}
