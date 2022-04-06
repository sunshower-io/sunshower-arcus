package com.aire.ux.select.css;

import static java.lang.String.format;

import com.aire.ux.plan.Evaluator;
import com.aire.ux.plan.Plan;
import com.aire.ux.plan.PlanContext;
import com.aire.ux.plan.PlanNode;
import com.aire.ux.plan.WorkingSet;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.AbstractSyntaxTree;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.reflect.Reflect;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import lombok.val;

public final class DefaultSelector implements Selector {

  private final AbstractSyntaxTree<Symbol, Token> tree;

  public DefaultSelector() {
    this.tree = new AbstractSyntaxTree<>();
  }

  DefaultSelector(AbstractSyntaxTree<Symbol, Token> tree) {
    this.tree = tree;
  }

  @Override
  public AbstractSyntaxTree<Symbol, Token> getSyntaxTree() {
    return tree;
  }

  @Override
  public List<SyntaxNode<Symbol, Token>> find(Predicate<SyntaxNode<Symbol, Token>> f) {
    return tree.reduce(
        new ArrayList<>(),
        (node, list) -> {
          if (f.test(node)) {
            list.add(node);
          }
          return list;
        });
  }

  @Override
  public Plan plan(PlanContext context) {
    return tree.reduce(new LinkedPlan(context), new PlanBuilder(context)).freeze();
  }

  static class LinkedPlanNode implements PlanNode {

    private final LinkedPlanNode next;
    private final Evaluator evaluator;

    public LinkedPlanNode(LinkedPlanNode next, Evaluator evaluator) {
      this.next = next;
      this.evaluator = evaluator;
    }
  }

  private static class PlanBuilder
      implements BiFunction<SyntaxNode<Symbol, Token>, LinkedPlan, LinkedPlan> {

    final PlanContext context;

    public PlanBuilder(PlanContext context) {
      this.context = context;
    }

    @Override
    public LinkedPlan apply(SyntaxNode<Symbol, Token> node, LinkedPlan plan) {
      return plan.prepend(context.lookup(node).create(node, context));
    }
  }

  private static final class LinkedPlan implements Plan {

    private final PlanContext context;
    private LinkedPlanNode head;

    LinkedPlan(@Nonnull final PlanContext context) {
      Objects.requireNonNull(context);
      this.context = context;
    }

    LinkedPlan(LinkedPlanNode head, PlanContext context) {
      Objects.requireNonNull(context);
      this.head = head;
      this.context = context;
    }

    final <T> T fold(LinkedPlanNode init, T value, BiFunction<LinkedPlanNode, T, T> f) {
      var result = value;
      for (var c = init; c != null; c = c.next) {
        result = f.apply(c, result);
      }
      return result;
    }

    public Plan freeze() {
      return fold(head, new LinkedPlan(context), (node, plan) -> plan.prepend(node.evaluator));
    }

    @Override
    public String toString() {
      val result = new StringBuilder();
      toString(head, result, 0);
      return result.toString();
    }

    private void toString(LinkedPlanNode node, StringBuilder result, int depth) {
      if (node != null) {
        val indent = " ".repeat(depth);
        val evaluator = indent + "└╴" + node.evaluator.toString();
        result.append(evaluator).append("").append("\n");
        toString(node.next, result, evaluator.length());
      }
    }

    LinkedPlan prepend(Evaluator evaluator) {
      if (head == null) {
        head = new LinkedPlanNode(null, evaluator);
      } else {
        val result = new LinkedPlanNode(head, evaluator);
        head = result;
      }
      return this;
    }

    @Override
    public <T extends Evaluator> List<T> getEvaluators(Class<T> evaluatorType) {
      return fold(
          head,
          new ArrayList<T>(),
          (node, list) -> {
            if (Reflect.isCompatible(evaluatorType, node.evaluator.getClass())) {
              list.add((T) node.evaluator);
            }
            return list;
          });
    }

    @Override
    public <T> WorkingSet<T> evaluate(T tree, NodeAdapter<T> hom) {
      val results = WorkingSet.of(tree);
      return evaluate(results, hom);
      //      return fold(head, results, (node, list) -> node.evaluator.evaluate(list, hom));
    }

    @Override
    public <T> WorkingSet<T> evaluate(WorkingSet<T> tree, NodeAdapter<T> hom) {
      return fold(head, tree, (node, list) -> node.evaluator.evaluate(list, hom));
    }

    @Override
    public void close() throws Exception {
      fold(
          head,
          null,
          (node, list) -> {
            if (node.evaluator instanceof AutoCloseable) {
              try {
                ((AutoCloseable) node.evaluator).close();
              } catch (Exception ex) {
                throw new IllegalStateException(
                    format("Unexpected exception: %s", ex.getMessage()), ex);
              }
            }
            return null;
          });
    }
  }
}
