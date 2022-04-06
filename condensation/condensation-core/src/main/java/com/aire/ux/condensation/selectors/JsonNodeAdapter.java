package com.aire.ux.condensation.selectors;

import com.aire.ux.condensation.json.Value;
import com.aire.ux.condensation.json.Value.Type;
import com.aire.ux.condensation.json.Values.ObjectValue;
import com.aire.ux.test.NodeAdapter;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.val;

public class JsonNodeAdapter implements NodeAdapter<SyntaxNode<Value<?>, Token>> {

  @Nonnull
  @Override
  public List<SyntaxNode<Value<?>, Token>> getChildren(
      @Nonnull SyntaxNode<Value<?>, Token> current) {
    return current.getChildren();
  }

  @Nullable
  @Override
  public SyntaxNode<Value<?>, Token> getParent(@Nonnull SyntaxNode<Value<?>, Token> current) {
    return current.getParent();
  }

  @Override
  @SuppressWarnings("unchecked")
  public SyntaxNode<Value<?>, Token> setChildren(
      @Nonnull SyntaxNode<Value<?>, Token> current,
      @Nonnull Collection<? extends SyntaxNode<Value<?>, Token>> children) {
    current.setChildren((Collection<SyntaxNode<Value<?>, Token>>) children);
    return current;
  }

  @Nullable
  @Override
  public String getAttribute(@Nonnull SyntaxNode<Value<?>, Token> c, @Nonnull String key) {
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
  public SyntaxNode<Value<?>, Token> setAttribute(
      SyntaxNode<Value<?>, Token> node, String key, String value) {
    return null;
  }

  @Override
  public boolean hasAttribute(SyntaxNode<Value<?>, Token> c, String key) {
    if (c != null && c.getValue().getType() == Type.Object) {
      return (((ObjectValue) c.getValue()).has(key));
    }
    return false;
  }

  @Override
  public SyntaxNode<Value<?>, Token> clone(SyntaxNode<Value<?>, Token> value) {
    return value.clone();
  }

  @Override
  public String getType(SyntaxNode<Value<?>, Token> n) {
    val v = n.getValue();
    if (v != null) {
      return v.getType().name().toLowerCase(Locale.ROOT);
    }
    return null;
  }

  @Override
  public void setState(@Nonnull SyntaxNode<Value<?>, Token> element, @Nonnull State state) {}

  @Override
  public boolean hasState(@Nonnull SyntaxNode<Value<?>, Token> element, @Nonnull State state) {
    return false;
  }

  @Nullable
  @Override
  public SyntaxNode<Value<?>, Token> getSucceedingSibling(
      @Nonnull SyntaxNode<Value<?>, Token> element) {
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
