package com.aire.ux.select.css;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.select.css.CssSelectorParser.ElementSymbol;
import io.sunshower.arcus.ast.AbstractSyntaxTree;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.Token;
import java.util.function.Predicate;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("com.aire.ux.select.scenarios.type")
public class CssSelectorParserTest {

  public static class TestCase {

    protected CssSelectorParser parser;

    public static void expectNodePropertyCount(
        AbstractSyntaxTree<Symbol, Token> tree,
        Predicate<SyntaxNode<Symbol, Token>> predicate,
        int i) {
      assertEquals(i, tree.reduce(0, (node, n) -> predicate.test(node) ? n + 1 : n));
    }

    public static void expectSymbolCount(
        AbstractSyntaxTree<Symbol, Token> tree, Symbol type, int i) {
      assertEquals(i, tree.reduce(0, (node, n) -> type.equals(node.getSymbol()) ? n + 1 : n));
    }

    public static boolean isCombinator(SyntaxNode<Symbol, Token> node) {
      val t = node.getSymbol();
      if (t instanceof ElementSymbol) {
        val type = (ElementSymbol) t;
        switch (type) {
          case ChildSelector:
          case AdjacentSiblingSelector:
          case GeneralSiblingSelector:
          case DescendantSelector:
            return true;
          default:
            return false;
        }
      }
      return false;
    }

    @BeforeEach
    protected void setUp() {
      parser = new CssSelectorParser();
    }
  }
}
