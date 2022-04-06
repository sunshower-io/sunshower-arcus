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
import java.util.Objects;
import lombok.val;

public class IdSelectorEvaluatorFactory extends AbstractMemoizingEvaluatorFactory {

  public IdSelectorEvaluatorFactory() {
    super(ElementSymbol.IdentitySelector);
  }

  @Override
  protected Evaluator createEvaluator(SyntaxNode<Symbol, Token> node, PlanContext context) {
    if (!node.hasChildren()) {
      throw new IllegalStateException(
          "Error: expected #<identifier>.  Got the identity selector but not the identifier");
    }
    val children = node.getChildren();
    if (children.size() != 1) {
      throw new IllegalStateException(
          format("Expected exactly one child <identifier>.  Got: %s", children));
    }

    /**
     * for class, id, etc. you have to remove the child ID type or it will be filtered in subsequent
     * evaluation epochs
     */
    val idNode = node.removeChild(0);
    return new IdSelectorEvaluator(node, idNode, context);
  }

  private static class IdSelectorEvaluator extends AbstractHierarchySearchingEvaluator {

    private final String id;

    public IdSelectorEvaluator(
        SyntaxNode<Symbol, Token> node, SyntaxNode<Symbol, Token> idNode, PlanContext context) {
      super(node, context);
      this.id = Objects.requireNonNull(idNode.getSource().getLexeme());
    }

    @Override
    protected <T> boolean appliesTo(NodeAdapter<T> hom, T n, WorkingSet<T> workingSet) {
      return id.equals(hom.getId(n)) && super.appliesTo(hom, n, workingSet);
    }
  }
}
