package com.aire.ux.plan;

import static com.aire.ux.select.css.CssSelectorToken.AdditionOperator;
import static com.aire.ux.select.css.CssSelectorToken.Identifier;
import static com.aire.ux.select.css.CssSelectorToken.Minus;
import static com.aire.ux.select.css.CssSelectorToken.Numeric;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;

import com.aire.ux.select.css.CssSelectorParser.CssSyntaxNode;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.LookaheadIterator;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.ast.core.TokenWord;
import io.sunshower.arcus.ast.core.Type;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nonnull;
import lombok.val;

@SuppressFBWarnings
public interface Expression extends Function<Integer, Integer> {

  /**
   * Evaluate this expression against the list of elements
   *
   * @param tokens the elements to evaluate this against
   * @return a possibly empty, never null list of elements
   */
  static Expression parse(List<SyntaxNode<Symbol, Token>> tokens) {
    val ts = LookaheadIterator.wrap(tokens.iterator());
    return parse(ts);
  }

  private static Expression parse(LookaheadIterator<SyntaxNode<Symbol, Token>> tokens) {
    boolean negated = isNegation(tokens);
    if (negated) {
      expectNegation(tokens);
    }
    val expression = readFunctionalPrelude(tokens);
    val lhs = negated ? negate(expression) : expression;
    if (is(tokens, AdditionOperator, Minus)) {
      return readFunctionalBody(lhs, tokens);
    } else {
      return lhs;
    }
  }

  static void expectNegation(LookaheadIterator<SyntaxNode<Symbol, Token>> tokens) {
    if (is(tokens, Minus)) {
      expect(tokens, Minus);
    } else {
      val next = tokens.next();
      val token = next.getSource();
      tokens.pushBack(
          new CssSyntaxNode(
              next.getSymbol(),
              new TokenWord(
                  token.getStart() + 1,
                  token.getEnd(),
                  token.getLexeme().substring(1),
                  token.getType())));
    }
  }

  /**
   * css requires a modal parser as (-n) is typically an identifier unless it appears in a
   * functional expression. However, we don't want to have to implement a more complex parser to
   * handle basically the only case. Therefore, check the lexeme to see if its first character is
   * '-' and, if so, treat it as a minus instead of the first character of an identifier token
   *
   * @param tokens the tokens to check
   * @return true if we're in a negation
   */
  static boolean isNegation(LookaheadIterator<SyntaxNode<Symbol, Token>> tokens) {
    if (is(tokens, Minus)) {
      return true;
    }
    val next = tokens.peek();
    if (next.getSource().getLexeme().charAt(0) == '-') {
      return true;
    }
    return false;
  }

  static Expression readFunctionalBody(
      Expression prelude, LookaheadIterator<SyntaxNode<Symbol, Token>> tokens) {
    return operator(prelude, tokens);
  }

  static Expression operator(Expression expr, LookaheadIterator<SyntaxNode<Symbol, Token>> tokens) {
    if (is(tokens, AdditionOperator)) {
      expect(tokens, AdditionOperator);
      return new AdditionExpression(expr, number(tokens));
    }
    expect(tokens, Minus);
    return new SubtractionExpression(expr, number(tokens));
  }

  /**
   * @param tokens the tokens to extrct the expression from
   * @return the expression
   */
  static Expression readFunctionalPrelude(LookaheadIterator<SyntaxNode<Symbol, Token>> tokens) {
    if (is(tokens, Numeric)) {
      val n = number(tokens);
      if (is(tokens, Identifier)) {
        return new AlgebraicExpression(n, variable(tokens));
      }
      return n;
    }
    if (is(tokens, Identifier)) {
      return new AlgebraicExpression(variable(tokens));
    }
    throw new IllegalStateException("not implemented yet");
  }

  static Expression variable(LookaheadIterator<SyntaxNode<Symbol, Token>> tokens) {
    return new Variable(expect(tokens, Identifier).getSource().getLexeme());
  }

  static Expression number(LookaheadIterator<SyntaxNode<Symbol, Token>> tokens) {
    return new Constant(parseInt(expect(tokens, Numeric).getSource().getLexeme()));
  }

  private static Expression negate(Expression expression) {
    return new Negation(expression);
  }

  private static SyntaxNode<Symbol, Token> expect(
      LookaheadIterator<SyntaxNode<Symbol, Token>> toks, Type... types) {
    if (!toks.hasNext()) {
      throw new IllegalArgumentException(
          format("Error: expected one of %s, got EOF", Arrays.toString(types)));
    }

    if (!is(toks, types)) {
      throw new IllegalArgumentException(
          format(
              "Expected one of %s, got '%s'",
              Arrays.toString(types), toks.peek().getSource().getType()));
    }
    return toks.next();
  }

  private static boolean is(LookaheadIterator<SyntaxNode<Symbol, Token>> tokens, Type... test) {
    if (!tokens.hasNext()) {
      return false;
    }
    val next = tokens.peek().getSource();
    for (val token : test) {
      if (token.equals(next.getType())) {
        return true;
      }
    }
    return false;
  }

  final class AlgebraicExpression implements Expression {

    final Expression constant;
    final Expression variable;

    public AlgebraicExpression(@Nonnull Expression variable) {
      this(new Constant(1), variable);
    }

    public AlgebraicExpression(@Nonnull Expression constant, @Nonnull Expression variable) {
      this.constant = constant;
      this.variable = variable;
    }

    public String toString() {
      return format("Expr(alg: [const: %s], [alg: %s])", constant, variable);
    }

    @Override
    public Integer apply(Integer integer) {
      return constant.apply(integer) * variable.apply(integer);
    }
  }

  class Constant implements Expression {

    final int value;

    Constant(int value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return format("Expr(const: %d)", value);
    }

    @Override
    public Integer apply(Integer integer) {
      return value;
    }
  }

  class Variable implements Expression {

    final String name;

    Variable(String name) {
      this.name = name;
    }

    @Override
    public String toString() {
      return format("Expr(variable: %s)", name);
    }

    @Override
    public Integer apply(Integer integer) {
      return integer;
    }
  }

  @SuppressFBWarnings
  final class Negation implements Expression {

    private final Expression expression;

    Negation(Expression expression) {
      this.expression = expression;
    }

    @Override
    public String toString() {
      return format("-%s", expression);
    }

    @Override
    public Integer apply(Integer integer) {
      return -expression.apply(integer);
    }
  }

  @SuppressFBWarnings
  final class AdditionExpression implements Expression {
    final Expression lhs;
    final Expression rhs;

    AdditionExpression(Expression lhs, Expression rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public String toString() {
      return format("%s + %s", lhs, rhs);
    }

    @Override
    public Integer apply(Integer integer) {
      return lhs.apply(integer) + rhs.apply(integer);
    }
  }

  @SuppressFBWarnings
  final class SubtractionExpression implements Expression {
    final Expression lhs;
    final Expression rhs;

    public SubtractionExpression(Expression lhs, Expression rhs) {
      this.lhs = lhs;
      this.rhs = rhs;
    }

    @Override
    public String toString() {
      return format("%s - %s", lhs, rhs);
    }

    @Override
    public Integer apply(Integer integer) {
      return lhs.apply(integer) - rhs.apply(integer);
    }
  }
}
