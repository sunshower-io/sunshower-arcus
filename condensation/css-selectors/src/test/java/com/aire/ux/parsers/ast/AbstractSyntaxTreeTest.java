package com.aire.ux.parsers.ast;

import com.aire.ux.select.css.CssSelectorParserTest.TestCase;
import io.sunshower.arcus.ast.AbstractSyntaxNode;
import io.sunshower.arcus.ast.AbstractSyntaxTree;
import io.sunshower.arcus.ast.AbstractSyntaxTree.Order;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.val;
import org.junit.jupiter.api.Test;

@SuppressWarnings("unchecked")
class AbstractSyntaxTreeTest extends TestCase {

  @Test
  void ensureSubtractiveRewritingWorks() {
    val root = node(1, node(2, node(3)));

    val tree = new AbstractSyntaxTree<>(List.of(root));
    val newTree =
        tree.rewrite(
            node ->
                node.getValue() == null
                    ? node
                    : node.getValue() % 2 == 0
                        ? node(node.getValue() * 2, node(node.getValue()))
                        : node(
                            node.getValue() * 3,
                            node(node.getValue() * 4, node(node.getValue() * 5))));

    System.out.println(tree);
    System.out.println(newTree);
  }

  @Test
  void ensureRewritingTreeWorks() {
    val root = node(1, node(2, node(3)));

    val tree = new AbstractSyntaxTree<>(List.of(root));
    val newTree =
        tree.rewrite(
            node -> {
              if (node.getValue() == null) {
                return node;
              } else {
                return node(node.getValue(), node(node.getValue() * 2), node(node.getValue() + 2));
              }
            });
    System.out.println(tree);
    System.out.println(newTree);
  }

  @Test
  void ensureRewritingSiblingsWorks() {
    val nodes = List.of(node("component"), node("value"));

    val tree = new AbstractSyntaxTree<>(nodes);
    AbstractSyntaxTree rewritten =
        tree.rewrite(
            node -> {
              val result = node.clone();
              val children = node.getChildren();
              for (int i = 0; i < children.size(); i++) {
                val component = children.get(i);
                if (component.getValue().equals("component") && i + 1 < children.size()) {
                  val next = children.get(i + 1);
                  if (next.getValue().equals("value")) {
                    node.removeChild(i);
                    node.removeChild(i);
                    component.addChild(next);
                    result.addChild(component);
                  }
                }
              }
              return result;
            });

    System.out.println(rewritten);
  }

  private <T, U> SyntaxNode<T, U> node(T value, SyntaxNode<T, U>... children) {
    val n = new AbstractSyntaxNode<T, U>(Symbol.symbol(String.valueOf(value)), null, value);
    n.setChildren(Arrays.asList(children));
    return n;
  }

  @Test
  void ensureIterationOrderIsCorrect() {
    val selector = parser.parse("div.hello > *.world");
    val preorderStream =
        StreamSupport.stream(selector.getSyntaxTree().iterate(Order.Pre).spliterator(), false)
            .collect(Collectors.toList());

    val postorderStream =
        StreamSupport.stream(selector.getSyntaxTree().iterate(Order.Post).spliterator(), false)
            .collect(Collectors.toList());

    val iter = preorderStream.iterator();
    val piter = postorderStream.iterator();
    while (piter.hasNext()) {
      //      System.out.println("PRE: " + iter.next());
      System.out.println("POS: " + piter.next());
    }
    System.out.println(selector.getSyntaxTree());
  }
}
