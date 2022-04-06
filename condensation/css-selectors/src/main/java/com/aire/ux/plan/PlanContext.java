package com.aire.ux.plan;

import com.aire.ux.select.css.Selector;
import com.aire.ux.test.NodeAdapter.State;
import io.sunshower.arcus.ast.AbstractSyntaxTree;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import lombok.val;

public interface PlanContext {

  public static void register(EvaluatorFactory factory, Symbol... symbols) {
    for (val symbol : symbols) {
      DefaultPlanContext.factories.put(symbol, factory);
    }
  }

  public static void register(EvaluatorFactory factory, State... symbols) {
    for (val symbol : symbols) {
      DefaultPlanContext.factories.put(symbol.toSymbol(), factory);
    }
  }

  default Selector create(SyntaxNode<Symbol, Token> root) {
    return Selector.create(new AbstractSyntaxTree<>(root));
  }

  /**
   * @param node the node to resolve an evaluatorfactory from
   * @return the evaluator
   * @throws java.util.NoSuchElementException if the node doesn't resolve to a corresponding
   *     evaluator factory
   */
  EvaluatorFactory lookup(SyntaxNode<Symbol, Token> node);
}
