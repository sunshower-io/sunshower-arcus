package io.sunshower.arcus.ast;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;

/**
 * Base class for a generic, homogeneous AST
 *
 * @param <T> the value contained (usually a symbol-like structure)
 * @param <U> the relevant source object (such as an element)
 */
public interface SyntaxNode<T, U> extends Cloneable {

  Symbol getSymbol();

  SyntaxNode<T, U> getParent();

  T getValue();

  U getSource();

  String getContent();

  List<SyntaxNode<T, U>> getChildren();

  boolean hasChildren();

  boolean addChild(SyntaxNode<T, U> child);

  void setContent(String content);

  String getProperty(String key);

  boolean hasProperty(String key);

  String setProperty(String key, String value);

  Map<String, String> getProperties();

  String clearProperty(String key);

  void addChildren(Collection<SyntaxNode<T, U>> children);

  /**
   * @param children the new children
   * @return the old children, if any
   */
  @Nonnull
  Collection<SyntaxNode<T, U>> setChildren(@Nonnull Collection<SyntaxNode<T, U>> children);

  SyntaxNode<T, U> getChild(int i);

  SyntaxNode<T, U> removeChild(int i);

  SyntaxNode<T, U> removeChild(SyntaxNode<T, U> i);

  List<SyntaxNode<T, U>> clearChildren();

  /**
   * perform a shallow-clone (i.e. don't clone children or parent nodes)
   *
   * @return the structural copy of this node, sans its original parents or children
   */
  @SuppressWarnings("PMD")
  SyntaxNode<T, U> clone();

  void setParent(SyntaxNode<T, U> tuAbstractSyntaxNode);
}
