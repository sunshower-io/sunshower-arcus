package com.aire.ux.condensation.json.selectors;

import com.aire.ux.condensation.json.JsonValue.Type;
import com.aire.ux.condensation.json.Values.ObjectValue;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.condensation.Value;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.val;

public class JsonNodeAdapter implements NodeAdapter<SyntaxNode<Value<?, Type>, Token>> {

  @Nonnull
  @Override
  public List<SyntaxNode<Value<?, Type>, Token>> getChildren(
      @Nonnull SyntaxNode<Value<?, Type>, Token> current) {
    return current.getChildren();
  }

  @Nullable
  @Override
  public SyntaxNode<Value<?, Type>, Token> getParent(
      @Nonnull SyntaxNode<Value<?, Type>, Token> current) {
    return current.getParent();
  }

  @Override
  @SuppressWarnings("unchecked")
  public SyntaxNode<Value<?, Type>, Token> setChildren(
      @Nonnull SyntaxNode<Value<?, Type>, Token> current,
      @Nonnull Collection<? extends SyntaxNode<Value<?, Type>, Token>> children) {
    current.setChildren((Collection<SyntaxNode<Value<?, Type>, Token>>) children);
    return current;
  }

  @Nullable
  @Override
  public String getAttribute(@Nonnull SyntaxNode<Value<?, Type>, Token> c, @Nonnull String key) {
    if (CLASS.equals(key)) {
      val v = c.getValue();
      if (v != null) {
        val value = v.getValue();
        if (value != null) {
          return value.toString();
        }
      }
    }
    if (c != null && c.getValue().getType() == Type.Object) {
      return (((ObjectValue) c.getValue()).get(key).toString());
    }

    return null;
  }

  @Override
  public SyntaxNode<Value<?, Type>, Token> setAttribute(
      SyntaxNode<Value<?, Type>, Token> node, String key, String value) {
    return null;
  }

  @Override
  public boolean hasAttribute(SyntaxNode<Value<?, Type>, Token> c, String key) {
    if (c != null && c.getValue().getType() == Type.Object) {
      return (((ObjectValue) c.getValue()).has(key));
    }
    return false;
  }

  @Override
  public SyntaxNode<Value<?, Type>, Token> clone(SyntaxNode<Value<?, Type>, Token> value) {
    return value.clone();
  }

  @Override
  public String getType(SyntaxNode<Value<?, Type>, Token> n) {
    val v = n.getValue();
    if (v != null) {
      return v.getType().name().toLowerCase(Locale.ROOT);
    }
    return null;
  }

  @Override
  public void setState(@Nonnull SyntaxNode<Value<?, Type>, Token> element, @Nonnull State state) {}

  @Override
  public boolean hasState(
      @Nonnull SyntaxNode<Value<?, Type>, Token> element, @Nonnull State state) {
    return false;
  }

  @Nullable
  @Override
  public SyntaxNode<Value<?, Type>, Token> getSucceedingSibling(
      @Nonnull SyntaxNode<Value<?, Type>, Token> element) {
    val parent = element.getParent();
    if (parent != null) {
      val childIdx = parent.getChildren().indexOf(element);
      if (childIdx != -1 && childIdx + 1 < parent.getChildren().size()) {
        return parent.getChild(childIdx + 1);
      }
    }
    return null;
  }

  @Override
  public State stateFor(String name) {
    return null;
  }
}
