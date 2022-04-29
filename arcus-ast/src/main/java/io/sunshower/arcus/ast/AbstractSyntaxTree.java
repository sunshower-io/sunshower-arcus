package io.sunshower.arcus.ast;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.function.BiFunction;
import javax.annotation.Nonnull;
import lombok.Getter;
import lombok.val;

@SuppressFBWarnings
public class AbstractSyntaxTree<T, U> implements Iterable<SyntaxNode<T, U>> {

  public static final Symbol ROOT_SYMBOL =
      new Symbol() {
        @Override
        public String toString() {
          return "RootSymbol";
        }
      };

  @Getter private final SyntaxNode<T, U> root;

  /**
   * construct a new AST with the provided root
   *
   * @param root the root of the tree
   */
  public AbstractSyntaxTree(SyntaxNode<T, U> root) {
    this.root = root;
  }

  /**
   * construct a new AST with an empty root node. This root-node is identified by the ROOT_SYMBOL
   * symbol
   */
  public AbstractSyntaxTree() {
    this(new RootSyntaxNode<>());
  }

  public AbstractSyntaxTree(List<SyntaxNode<T, U>> children) {
    this();
    root.setChildren(children);
  }

  @Override
  public String toString() {
    val result = new StringBuilder();
    toString(root, result, "", true);
    return result.toString();
  }

  /**
   * @param initial the initial value (nonnul)
   * @param f the function to reduce the abstract syntax tree over
   * @param <V> the type-parameter of the initial value (and result)
   * @return the result of reducing the AST over the function
   */
  public <V> V reduce(@Nonnull V initial, @Nonnull BiFunction<SyntaxNode<T, U>, V, V> f) {
    return reduce(Order.Pre, initial, f);
  }

  /**
   * @param order reduce in either pre-order or post-order
   * @param initial the initial value
   * @param f the function to reduce this abstract syntax tree over
   * @param <V> the type of the initial value (and result)
   * @return the reduction result
   */
  public <V> V reduce(
      @Nonnull Order order, @Nonnull V initial, @Nonnull BiFunction<SyntaxNode<T, U>, V, V> f) {
    val struct = new IterationStructure<T, U>(order);
    struct.add(root);
    var result = initial;
    while (!struct.isEmpty()) {
      val iterator = struct.listIterator();
      while (iterator.hasNext()) {
        val next = iterator.next();
        result = f.apply(next, result);
        iterator.remove();
        for (val c : next.getChildren()) {
          iterator.add(c);
        }
      }
    }
    return result;
  }

  /**
   * apply a tree-rewriting rule to this tree and return the rewritten tree, leaving this tree
   * unmodified
   *
   * @param rewriteRule the rewrite rule to apply
   * @return a new syntax tree with the rewrite rule applies
   */
  public AbstractSyntaxTree<T, U> rewrite(@Nonnull RewriteRule<T, U> rewriteRule) {
    val result = new ArrayDeque<SyntaxNode<T, U>>();
    result.push(root);

    val rewrittenRoot = rewriteRule.apply(root);
    val rewritten = new ArrayDeque<SyntaxNode<T, U>>();
    rewritten.push(rewrittenRoot);
    while (!result.isEmpty()) {
      val next = result.pop();
      val rewrittenNext = rewritten.pop();
      for (val child : List.copyOf(next.getChildren())) {
        val rewrittenChild = rewriteRule.apply(child);
        rewritten.push(rewrittenChild);
        rewrittenNext.addChild(rewrittenChild);
        result.add(child);
      }
    }
    return new AbstractSyntaxTree<>(rewrittenRoot);
  }

  /**
   * generate an interable over this AST in the specified order
   *
   * @param order
   * @return
   */
  public Iterable<SyntaxNode<T, U>> iterate(Order order) {
    val struct = new IterationStructure<T, U>(order);
    struct.add(root);
    return new Iterable<SyntaxNode<T, U>>() {
      @Nonnull
      @Override
      public Iterator<SyntaxNode<T, U>> iterator() {
        return new Iterator<>() {
          @Override
          public boolean hasNext() {
            return !struct.isEmpty();
          }

          @Override
          public SyntaxNode<T, U> next() {
            val next = struct.remove();
            struct.addAll(next.getChildren());
            return next;
          }
        };
      }
    };
  }

  @Nonnull
  @Override
  public Iterator<SyntaxNode<T, U>> iterator() {
    return iterate(Order.Pre).iterator();
  }

  private void toString(SyntaxNode<T, U> node, StringBuilder out, String indent, boolean last) {
    if (node.equals(root)) {
      out.append(node).append("\n");
    } else {
      out.append(indent).append(last ? "└╴" : "├╴").append(node).append("\n");
    }
    indent = indent + (last ? "   " : "│  ");
    val iter = node.getChildren().iterator();
    while (iter.hasNext()) {
      val child = iter.next();
      val isLast = !iter.hasNext();
      toString(child, out, indent, isLast);
    }
  }

  @Override
  @SuppressWarnings("unchecked")
  public int hashCode() {

    var result = 31;
    for (val node : this) {
      result = 37 * Objects.hashCode(node) + result;
    }
    return result;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null) {
      return false;
    }

    if (o instanceof AbstractSyntaxTree) {
      val ot = (AbstractSyntaxTree<T, U>) o;
      val oti = ot.iterator();
      val ti = iterator();

      while (ti.hasNext()) {
        if (!oti.hasNext()) {
          return false;
        }
        val tnext = ti.next();
        val onext = oti.next();
        if (!Objects.equals(tnext, onext)) {
          return false;
        }
      }
    }
    return true;
  }

  public enum Order {
    Pre,
    Post
  }

  static final class RootSyntaxNode<T, U> extends AbstractSyntaxNode<T, U> {

    public RootSyntaxNode() {
      super(ROOT_SYMBOL, null, null, null);
    }

    @Override
    public RootSyntaxNode<T, U> clone() {
      return new RootSyntaxNode<>();
    }

    @Override
    public String toString() {
      return "RootNode";
    }
  }

  private static final class IterationStructure<T, U> implements Iterable<SyntaxNode<T, U>> {

    final Order order;
    final LinkedList<SyntaxNode<T, U>> deque;

    private IterationStructure(Order order) {
      this.order = order;
      this.deque = new LinkedList<>();
    }

    void add(SyntaxNode<T, U> node) {
      if (order == Order.Pre) {
        deque.push(node);
      } else {
        deque.offer(node);
      }
    }

    SyntaxNode<T, U> remove() {
      return order == Order.Pre ? deque.pop() : deque.poll();
    }

    ListIterator<SyntaxNode<T, U>> listIterator() {
      return order == Order.Pre ? deque.listIterator() : deque.listIterator(deque.size());
    }

    boolean isEmpty() {
      return deque.isEmpty();
    }

    void addAll(Collection<? extends SyntaxNode> nodes) {
      for (val n : nodes) {
        add(n);
      }
    }

    @Nonnull
    @Override
    public Iterator<SyntaxNode<T, U>> iterator() {
      return listIterator();
    }
  }
}
