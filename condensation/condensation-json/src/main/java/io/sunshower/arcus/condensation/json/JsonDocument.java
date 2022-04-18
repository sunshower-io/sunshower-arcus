package io.sunshower.arcus.condensation.json;

import io.sunshower.arcus.ast.AbstractSyntaxTree;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.condensation.Document;
import io.sunshower.arcus.condensation.TypeBinder;
import io.sunshower.arcus.condensation.Value;
import io.sunshower.arcus.condensation.json.JsonValue.Type;
import io.sunshower.arcus.condensation.json.selectors.JsonNodeAdapter;
import io.sunshower.arcus.selectors.css.CssSelectorParser;
import io.sunshower.arcus.selectors.plan.DefaultPlanContext;
import io.sunshower.arcus.selectors.test.NodeAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Supplier;
import lombok.val;

public class JsonDocument implements Document<Type> {

  static final NodeAdapter<SyntaxNode<Value<?, Type>, Token>> nodeAdapter;

  static {
    nodeAdapter = new JsonNodeAdapter();
  }

  private final CssSelectorParser selectorParser;
  private final AbstractSyntaxTree<Value<?, Type>, Token> tree;

  public JsonDocument(AbstractSyntaxTree<Value<?, Type>, Token> parse) {
    super();
    this.tree = parse;
    this.selectorParser = new CssSelectorParser();
  }

  @Override
  @SuppressWarnings("unchecked")
  public <U, T extends Value<U, Type>> T getRoot() {
    return (T) tree.getRoot().getValue().getValue();
  }

  @Override
  public <U> U get(String key) {
    return null;
  }

  @Override
  public <U, T extends Value<U, Type>> T getValue(String key) {
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T select(String selector) {
    val workingSet =
        selectorParser
            .parse(selector)
            .plan(DefaultPlanContext.getInstance())
            .evaluate(tree.getRoot(), nodeAdapter);
    if (workingSet.size() > 0) {
      val next = workingSet.iterator().next();
      return valueFor(next);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private <T> T valueFor(SyntaxNode<Value<?, Type>, Token> next) {
    if (next.hasChildren()) {
      val child = next.getChild(0);
      if (child != null) {
        val childValue = child.getValue();
        if (childValue != null) {
          return (T) childValue.getValue();
        }
      }
    } else {
      val childValue = next.getValue();
      if (childValue != null) {
        return (T) childValue.getValue();
      }
    }
    return null;
  }

  @Override
  public Collection<?> selectAll(String selector) {
    val workingSet =
        selectorParser
            .parse(selector)
            .plan(DefaultPlanContext.getInstance())
            .evaluate(tree.getRoot(), nodeAdapter);
    val result = new ArrayList<>(workingSet.size());
    for (val c : workingSet) {
      result.add(valueFor(c));
    }
    return result;
  }

  @Override
  public <T> T read(Class<T> type, TypeBinder<Type> strategy) {
    return strategy.bind(type, tree.getRoot());
  }

  @Override
  public <U extends Collection<? super T>, T> U readAll(
      Class<T> type, Supplier<U> instantiator, TypeBinder<Type> strategy) {
    val root = tree.getRoot();
    if (root == null) {
      return null;
    }
    val value = root.getValue();
    if (value == null) {
      return null;
    }
    val result = instantiator.get();
    if (value.getType() == Type.Array) {
      for (val child : root.getChildren()) {
        result.add(strategy.bind(type, child));
      }
      return result;
    }
    result.add(strategy.bind(type, root));
    return result;
  }
}
