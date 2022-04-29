package io.sunshower.arcus.ast;

import static java.lang.String.format;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import lombok.val;

@SuppressFBWarnings
public class AbstractSyntaxNode<T, U> implements SyntaxNode<T, U> {

  /**
   * immutable state
   */
  final Symbol symbol;

  final T value;
  final U source;
  final Map<String, String> properties;
  final List<SyntaxNode<T, U>> children;
  private SyntaxNode<T, U> parent;
  /**
   * private state
   */
  private String content;

  public AbstractSyntaxNode(Symbol symbol, U source, T value) {
    this(symbol, source, null, value, new ArrayList<>());
  }

  public AbstractSyntaxNode(SyntaxNode<T, U> parent, Symbol symbol, U source, T value) {
    this(parent, symbol, source, null, value, new ArrayList<>());
  }

  /**
   * @param symbol  the associated symbol (element type)
   * @param source  the language element this was retrieved from
   * @param content the String content (if any)
   * @param value   the actual value node (if any)
   */
  public AbstractSyntaxNode(
      SyntaxNode<T, U> parent, Symbol symbol, U source, String content, T value) {
    this(parent, symbol, source, content, value, new ArrayList<>());
  }

  public AbstractSyntaxNode(Symbol symbol, U source, String content, T value) {
    this(null, symbol, source, content, value, new ArrayList<>());
  }

  public AbstractSyntaxNode(
      Symbol symbol, U source, String content, T value, List<SyntaxNode<T, U>> children) {
    this(null, symbol, source, content, value, children, new LinkedHashMap<>());
  }

  public AbstractSyntaxNode(
      SyntaxNode<T, U> parent,
      Symbol symbol,
      U source,
      String content,
      T value,
      List<SyntaxNode<T, U>> children) {
    this(parent, symbol, source, content, value, children, new LinkedHashMap<>());
  }

  public AbstractSyntaxNode(
      Symbol symbol,
      U source,
      String content,
      T value,
      List<SyntaxNode<T, U>> children,
      Map<String, String> properties) {
    this(null, symbol, source, content, value, children, properties);
  }

  public AbstractSyntaxNode(
      SyntaxNode<T, U> parent,
      Symbol symbol,
      U source,
      String content,
      T value,
      List<SyntaxNode<T, U>> children,
      Map<String, String> properties) {
    this.parent = parent;
    this.symbol = symbol;
    this.source = source;
    this.content = content;
    this.value = value;
    this.children = children;
    this.properties = properties;
  }

  @Override
  public Symbol getSymbol() {
    return symbol;
  }

  @Override
  public SyntaxNode<T, U> getParent() {
    return parent;
  }

  @Override
  public void setParent(SyntaxNode<T, U> parent) {
    this.parent = parent;
  }

  @Override
  public T getValue() {
    return value;
  }

  @Override
  public U getSource() {
    return source;
  }

  @Override
  public String getContent() {
    return content;
  }

  @Override
  public void setContent(String content) {
    this.content = content;
  }

  @Override
  public String getProperty(String key) {
    return properties.get(key);
  }

  @Override
  public boolean hasProperty(String key) {
    return properties.containsKey(key);
  }

  @Override
  public String setProperty(String key, String value) {
    return properties.put(key, value);
  }

  @Override
  public Map<String, String> getProperties() {
    return Collections.unmodifiableMap(properties);
  }

  @Override
  public String clearProperty(String key) {
    return properties.remove(key);
  }

  @Override
  public void addChildren(Collection<SyntaxNode<T, U>> children) {
    this.children.addAll(children);
  }

  @Nonnull
  @Override
  public Collection<SyntaxNode<T, U>> setChildren(@Nonnull Collection<SyntaxNode<T, U>> children) {
    val newChildren = List.copyOf(this.children);
    this.children.clear();
    for (val child : children) {
      addChild(child);
    }
    return newChildren;
  }

  @Override
  public SyntaxNode<T, U> getChild(int i) {
    return children.get(i);
  }

  @Override
  public SyntaxNode<T, U> removeChild(int i) {
    return children.remove(i);
  }

  @Override
  public SyntaxNode<T, U> removeChild(SyntaxNode<T, U> i) {
    return children.remove(children.indexOf(i));
  }

  @Override
  public List<SyntaxNode<T, U>> clearChildren() {
    val results = List.copyOf(children);
    children.clear();
    return results;
  }

  @Override
  public List<SyntaxNode<T, U>> getChildren() {
    return Collections.unmodifiableList(children);
  }

  @Override
  public boolean hasChildren() {
    return !children.isEmpty();
  }

  @Override
  public boolean addChild(SyntaxNode<T, U> child) {
    if (child != null) {
      child.setParent(this);
      return children.add(child);
    }
    return false;
  }

  /**
   * @return a shallow copy of this node (i.e. discards hierarchical structure)
   */
  @Override
  @SuppressFBWarnings
  @SuppressWarnings("PMD")
  public SyntaxNode<T, U> clone() {
    return new AbstractSyntaxNode<T, U>(
        symbol, source, content, value, new ArrayList<>(), new LinkedHashMap<>(properties));
  }

  @Override
  public String toString() {
    return format("Node[value: %s]", value);
  }


  @Override
  public int hashCode() {
    return Objects.hash(symbol, value, content, source);
  }

  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o == this) {
      return true;
    }

    if (o instanceof AbstractSyntaxNode) {
      val on = (AbstractSyntaxNode<T, U>) o;
      return Objects.equals(this.symbol, on.symbol)
             && Objects.equals(this.source, on.source)
             && Objects.equals(this.value, on.value);
    }
    return false;
  }
}
