package com.aire.ux.test;

import com.aire.ux.plan.WorkingSet;
import io.sunshower.arcus.ast.Symbol;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.val;

/**
 * adapts tree-like structures to structures that are understood by the planner
 *
 * @param <T> the type of the tree-like structure
 */
public interface NodeAdapter<T> {

  /** id constant */
  String ID = "id";

  /** class constant */
  String CLASS = "class";

  /**
   * map a tree-like structure to a tree-like structure
   *
   * @param root the root of the first tree
   * @param hom the hom functor used to perform the mapping
   * @param f the morphism {@code T -> U}
   * @param <U> the type of the second structure
   * @return a "hierarchy" encoded by <code>hom</code>
   */
  default <U> U map(
      @Nonnull final T root,
      @Nonnull final NodeAdapter<U> hom,
      @Nonnull final BiFunction<T, NodeAdapter<U>, U> f) {
    return hom.setChildren(
        f.apply(root, hom),
        getChildren(root).stream().map(c -> map(c, hom, f)).collect(Collectors.toList()));
  }

  /**
   * @param current the root node
   * @param initial the initial value to reduce over
   * @param f the reducer function
   * @param <U> the type-parameter of the result
   * @return the hierarchy reduced over the reducer function in breadth-first order
   */
  default <U> U reduce(
      @Nonnull final T current, @Nonnull final U initial, @Nonnull final BiFunction<T, U, U> f) {
    var result = initial;
    val stack = new ArrayDeque<T>();
    stack.add(current);
    while (!stack.isEmpty()) {
      val c = stack.poll();
      result = f.apply(c, result);
      for (val child : getChildren(c)) {
        if (child != null) {
          stack.add(child);
        }
      }
    }
    return result;
  }

  /**
   * @param workingSet
   * @param initial
   * @param f
   * @param <U>
   * @return
   */
  default <U> U reduce(
      @Nonnull final WorkingSet<T> workingSet,
      @Nonnull final U initial,
      final BiFunction<T, U, U> f) {
    val stack = new ArrayDeque<T>();
    stack.addAll(workingSet.results());
    var result = initial;
    while (!stack.isEmpty()) {
      val c = stack.poll();
      result = f.apply(c, result);
      for (val child : getChildren(c)) {
        if (child != null) {
          stack.add(child);
        }
      }
    }
    return result;
  }

  /**
   * @param current the node to retrieve the children of
   * @return the children, or an empty list if none exist
   */
  @Nonnull
  List<T> getChildren(@Nonnull T current);

  /**
   * @param current the node to get the siblings of
   * @return the siblings, or an empty list if there are none (i.e. root node)
   */
  @Nonnull
  default List<T> getSiblings(@Nonnull T current) {
    val parent = getParent(current);
    if (parent != null) {
      val children = getChildren(parent);
      return children.stream()
          .filter(child -> !Objects.equals(child, this))
          .collect(Collectors.toList());
    }
    return Collections.emptyList();
  }

  /**
   * @param current the node to retrieve the preceeding siblings of
   * @return the preceeding siblings, or an empty list if there are none
   */
  @Nonnull
  default List<T> getPreceedingSiblings(@Nonnull T current) {
    val parent = getParent(current);
    if (parent != null) {
      val iter = getChildren(parent).iterator();
      val result = new ArrayList<T>();
      while (iter.hasNext()) {
        val next = iter.next();
        if (Objects.equals(next, current)) {
          return result;
        } else {
          result.add(next);
        }
      }
    }
    return Collections.emptyList();
  }

  /**
   * @param current the node to retrieve the preceeding siblings of
   * @return the preceeding siblings, or an empty list if there are none
   */
  @Nonnull
  default List<T> getSucceedingSiblings(@Nonnull T current) {
    val parent = getParent(current);
    if (parent != null) {
      val children = getChildren(parent);
      val idx = children.indexOf(current);
      if (idx >= 0) {
        return children.subList(idx + 1, children.size());
      }
    }
    return Collections.emptyList();
  }

  /**
   * @param current the node to retrieve the parent of
   * @return the parent, or null if current is the root of the hierarchy
   */
  @Nullable
  T getParent(@Nonnull T current);

  /**
   * @param current the current node to set the children of
   * @param children the new children
   * @return the node
   */
  T setChildren(@Nonnull T current, @Nonnull Collection<? extends T> children);

  /**
   * @param current the node to retrieve the attribute value for
   * @param key the attribute key
   * @return the attribute value, or null
   */
  @Nullable
  String getAttribute(@Nonnull T current, @Nonnull String key);

  /**
   * @param current the current node to retrieve the ID of
   * @return the node's ID, or null
   */
  @Nullable
  default String getId(T current) {
    return getAttribute(current, ID);
  }

  /**
   * @param current the node to retrieve the class of
   * @return the node's class value, or null if it isn't set
   */
  default String getClass(T current) {
    return getAttribute(current, CLASS);
  }

  /**
   * @param node the node to set the attribute on
   * @param key the attribute key
   * @param value the attribute value
   * @return the node
   */
  T setAttribute(T node, String key, String value);

  /**
   * @param c the node to determine if the attribute exists for
   * @param key the attribute key
   * @return true if the key exists
   */
  boolean hasAttribute(T c, String key);

  /**
   * @param value the value to clone
   * @return a complete scalar copy (i.e. the children are not preserved)
   */
  T clone(T value);

  /**
   * @param n the node
   * @return the type of the node
   */
  String getType(T n);

  void setState(@Nonnull T element, @Nonnull State state);

  /**
   * @param state
   * @return whether the state exists on this element
   */
  boolean hasState(@Nonnull T element, @Nonnull State state);

  /**
   * @param element the element to retrieve
   * @return the next sibling, or null if none exists
   */
  @Nullable
  T getSucceedingSibling(@Nonnull T element);

  State stateFor(String name);

  public static interface State {

    int ordinal();

    Symbol toSymbol();

    /** @return the string representation of the state */
    String toString();

    /** @return the hashcode for this state */
    int hashCode();

    /**
     * @param o
     * @return true if this is equal to o, false otherwise
     */
    boolean equals(Object o);
  }
}
