package com.aire.ux.select.css;

import static com.aire.ux.select.css.CssSelectorToken.AdditionOperator;
import static com.aire.ux.select.css.CssSelectorToken.ApplicationEnd;
import static com.aire.ux.select.css.CssSelectorToken.AttributeGroup;
import static com.aire.ux.select.css.CssSelectorToken.AttributeGroupEnd;
import static com.aire.ux.select.css.CssSelectorToken.AttributeValueInSetOperator;
import static com.aire.ux.select.css.CssSelectorToken.DashedPrefixOperator;
import static com.aire.ux.select.css.CssSelectorToken.Dimension;
import static com.aire.ux.select.css.CssSelectorToken.FunctionStart;
import static com.aire.ux.select.css.CssSelectorToken.GreaterThan;
import static com.aire.ux.select.css.CssSelectorToken.Identifier;
import static com.aire.ux.select.css.CssSelectorToken.IdentifierSelector;
import static com.aire.ux.select.css.CssSelectorToken.Minus;
import static com.aire.ux.select.css.CssSelectorToken.Not;
import static com.aire.ux.select.css.CssSelectorToken.Numeric;
import static com.aire.ux.select.css.CssSelectorToken.PrefixOperator;
import static com.aire.ux.select.css.CssSelectorToken.PseudoClass;
import static com.aire.ux.select.css.CssSelectorToken.PseudoElement;
import static com.aire.ux.select.css.CssSelectorToken.StrictEqualityOperator;
import static com.aire.ux.select.css.CssSelectorToken.String;
import static com.aire.ux.select.css.CssSelectorToken.SubstringOperator;
import static com.aire.ux.select.css.CssSelectorToken.SuffixOperator;
import static com.aire.ux.select.css.CssSelectorToken.Tilde;
import static com.aire.ux.select.css.CssSelectorToken.Universal;
import static com.aire.ux.select.css.CssSelectorToken.Whitespace;
import static io.sunshower.arcus.ast.core.LookaheadIterator.wrap;
import static java.lang.String.format;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.ast.NamedSyntaxNode;
import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.LookaheadIterator;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.ast.core.TokenWord;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import lombok.val;

/**
 * selector parser: this class provides support for parsing css selectors. This current
 * implementation parses the prospective CSS Level 4 syntax with some extensions
 *
 * <p>refer to https://www.w3.org/TR/2018/REC-selectors-3-20181106/#grammar
 */
@ThreadSafe
@SuppressFBWarnings
public class CssSelectorParser {

  /** symbol for the tokenless element group--provided for convenience */
  static final Symbol GROUP = new Symbol() {};

  /** map from tokens to their corresponding symbols */
  private static final Map<CssSelectorToken, ElementSymbol> mappedTokens;

  /** construct the mappings from symbols to tokens */
  static {
    mappedTokens = new EnumMap<>(CssSelectorToken.class);
    for (val t : ElementSymbol.values()) {
      if (t.token != null) {
        mappedTokens.put(t.token, t);
      }
    }
  }

  /** the lexer--assumed to not be null */
  @Nonnull private final SelectorLexer lexer;

  /** create a new selector parser with the provided lexer */
  public CssSelectorParser() {
    this(new DefaultCssSelectorLexer());
  }

  /**
   * override the default selector lexer for this parser
   *
   * @param lexer
   */
  public CssSelectorParser(@Nonnull final SelectorLexer lexer) {
    Objects.requireNonNull(lexer, "lexer must not be null!");
    this.lexer = lexer;
  }

  /**
   * lookup a symbol from its corresponding token
   *
   * @param token the token to resolve a symbol for
   * @return the symbol, if it exists
   * @throws NoSuchElementException if there is no symbol for the token
   */
  static ElementSymbol symbolForToken(CssSelectorToken token) {
    val result = mappedTokens.get(token);
    if (result == null) {
      throw new NoSuchElementException("Error: no associated symbol for token: " + token);
    }
    return result;
  }

  /**
   * @param sequence the character sequence to extract the selector AST from
   * @return the Selector AST
   * @throws IllegalArgumentException if the syntax is invalid
   */
  public Selector parse(CharSequence sequence) {
    val tokens = wrap(lexer.lex(sequence).iterator());
    val result = new DefaultSelector();
    val ast = result.getSyntaxTree().getRoot();
    val child = selectorGroup(tokens);
    ast.addChildren(child);
    return result;
  }

  /**
   * parse the selector grouping
   *
   * @param tokens the LL(1), pushback-enabled token iterator
   * @return the list of selector groups
   * @throws IllegalArgumentException if the syntax is invalid
   */
  private List<SyntaxNode<Symbol, Token>> selectorGroup(LookaheadIterator<Token> tokens) {
    val result = new LinkedList<SyntaxNode<Symbol, Token>>();
    val union = new CssSyntaxNode(ElementSymbol.Union);
    var selector = new CssSyntaxNode(ElementSymbol.SelectorGroup);
    selector.addChildren(selector(tokens));
    union.addChild(selector);
    while (tokens.hasNext() && tokens.peek().getType() == CssSelectorToken.Comma) {
      union(tokens);
      eatWhitespace(tokens);
      selector = new CssSyntaxNode(ElementSymbol.SelectorGroup);
      selector.addChildren(selector(tokens));
      union.addChild(selector);
    }
    result.add(union);
    return result;
  }

  /**
   * parse the selector component of the grammar
   *
   * @param tokens the token stream
   * @return a list of selectors
   * @throws IllegalArgumentException if the syntax is invalid
   */
  private List<SyntaxNode<Symbol, Token>> selector(LookaheadIterator<Token> tokens) {
    val result = new LinkedList<SyntaxNode<Symbol, Token>>();
    result.addAll(simpleSelectorSequence(tokens));
    while (tokens.hasNext()) {
      val next = tokens.peek();
      if (next.getType() == CssSelectorToken.Comma) {
        break;
      } else if (isSequence(tokens)) {
        while (isSequence(tokens)) {
          result.addAll(simpleSelectorSequence(tokens));
        }
      } else if (isCombinator(tokens)) {
        var current = result.peekLast();
        while (isCombinator(tokens)) {
          val combinator = combinator(tokens);

          current.addChild(combinator);
          combinator.addChildren(simpleSelectorSequence(tokens));
          current = combinator;
        }
      }
    }
    return result;
  }

  /**
   * parse a simple selector sequence
   *
   * @param tokens the token stream
   * @return the list of selector-sequences
   * @throws IllegalArgumentException if the syntax is invalid
   */
  private List<SyntaxNode<Symbol, Token>> simpleSelectorSequence(LookaheadIterator<Token> tokens) {
    val result = new ArrayList<SyntaxNode<Symbol, Token>>();
    if (nextIs(tokens, Identifier, Universal)) {
      val next = tokens.next();
      val nextType = (CssSelectorToken) next.getType();
      result.add(new CssSyntaxNode(symbolForToken(nextType), next));
      while (isComposite(tokens)) {
        composite(tokens, result);
      }
      return result;
    } else if (isComposite(tokens)) {
      while (isComposite(tokens)) {
        composite(tokens, result);
      }
      return result;
    }

    if (tokens.hasNext()) {
      val next = tokens.next();
      throw new IllegalArgumentException(
          format(
              "Unexpected token '%s' at (%d, %d), lexeme: %s",
              next.getType(), next.getStart(), next.getEnd(), next.getLexeme()));
    }
    throw new IllegalStateException(
        format(
            "Bad state.  Remaining tokens: [%s]",
            tokens.toStream().map(t -> t.toString()).collect(Collectors.joining(","))));
  }

  /**
   * @param tokens the token sequence to check
   * @return true if the next token is a composite member
   */
  private boolean isComposite(LookaheadIterator<Token> tokens) {
    return nextIs(
        tokens,
        IdentifierSelector,
        CssSelectorToken.Class,
        AttributeGroup,
        PseudoClass,
        PseudoElement,
        FunctionStart,
        Not);
  }

  /**
   * check the next token to verify that it's the expected one, and, if it is read it from the
   * stream and discard it
   *
   * @param tokens the token-stream
   * @param token the expected token
   * @throws IllegalArgumentException if the next token in the token-stream is not equal to the
   *     expected token
   */
  private void expectAndDiscard(LookaheadIterator<Token> tokens, CssSelectorToken token) {
    if (!tokens.hasNext()) {
      throw new IllegalArgumentException(
          format("Error: expected %s (%s), got EOF", token, token.getRegularExpression()));
    }

    val next = tokens.next();
    val type = next.getType();

    if (token != type) {
      throw new IllegalArgumentException(
          format(
              "Error: expected %s, got %s at (%d, %d): lexeme: %s",
              token, type, next.getStart(), next.getEnd(), next.getLexeme()));
    }
  }

  /**
   * parse a composite rule. This does not correspond to a location in the grammar
   *
   * @param tokens the token stream
   * @param result the list of syntax-nodes at the current level of the syntax-tree
   */
  private void composite(LookaheadIterator<Token> tokens, List<SyntaxNode<Symbol, Token>> result) {
    var t = tokens.peek();
    var type = (CssSelectorToken) t.getType();
    if (type == AttributeGroup) {
      while (tokens.hasNext() && type == AttributeGroup) {
        parseAttributeGroup(tokens, result);
        if (tokens.hasNext()) {
          t = tokens.peek();
          type = (CssSelectorToken) t.getType();
        }
      }
    } else if (type == PseudoClass || type == PseudoElement) {
      pseudo(tokens, result);
    } else if (type == Not) {
      negation(tokens, result);
    } else {
      val selector = new CssSyntaxNode(symbolForToken(type), tokens.next());
      result.add(selector);
      selector.addChild(expect(tokens, Identifier, CssSelectorToken.Class));
    }
  }

  /**
   * parse a negation operator. It's actually a little easier to allow this method to parse more
   * complex expressions than those permitted by the CSS L4 spec
   *
   * @param tokens the token stream
   * @param result the list of syntax-nodes at the current level of the abstract syntax tree
   * @throws IllegalArgumentException if the syntax of the expression is not correct
   */
  private void negation(LookaheadIterator<Token> tokens, List<SyntaxNode<Symbol, Token>> result) {
    val negation = expect(tokens, Not);
    eatWhitespace(tokens);
    if (isSequence(tokens)) {
      var current = negation;
      var next = simpleSelectorSequence(tokens);
      current.addChildren(next);
      while (isCombinator(tokens)) {
        val combinator = combinator(tokens);
        current.addChild(combinator);
        combinator.addChildren(simpleSelectorSequence(tokens));
        current = combinator;
      }
    } else if (nextIs(tokens, AttributeGroup)) {
      val results = new ArrayList<SyntaxNode<Symbol, Token>>();
      parseAttributeGroup(tokens, results);
      negation.addChildren(results);
    } else if (nextIs(tokens, PseudoElement, PseudoClass)) {
      val results = new ArrayList<SyntaxNode<Symbol, Token>>();
      pseudo(tokens, results);
      negation.addChildren(results);
    }
    eatWhitespace(tokens);
    expectAndDiscard(tokens, ApplicationEnd);
    result.add(negation);
  }

  /**
   * parse a pseudo-element, class, or function invocation
   *
   * @param tokens the token stream
   * @param result the node list at the current level of the AST
   */
  private void pseudo(LookaheadIterator<Token> tokens, List<SyntaxNode<Symbol, Token>> result) {
    val pseudo = expect(tokens, PseudoClass, PseudoElement);
    result.add(pseudo);
    val next = (CssSelectorToken) tokens.peek().getType();
    if (next == FunctionStart) {
      pseudo.addChild(expression(tokens));
    } else {
      val id = expect(tokens, Identifier);
      val actualId = new CssSyntaxNode(Symbol.symbol(id.getSource().getLexeme()), id.getSource());
      pseudo.addChild(actualId);
    }
  }

  /**
   * parse an expression
   *
   * @param tokens the tokens
   * @return a syntax-node containing the expression contents as children
   */
  private SyntaxNode<Symbol, Token> expression(LookaheadIterator<Token> tokens) {
    eatWhitespace(tokens);
    val function = functionNode(expect(tokens, FunctionStart));
    while (nextIs(tokens, AdditionOperator, Minus, Dimension, Numeric, String, Identifier)) {
      function.addChild(
          expect(tokens, AdditionOperator, Minus, Dimension, Numeric, String, Identifier));
      eatWhitespace(tokens);
    }
    expectAndDiscard(tokens, ApplicationEnd);
    return function;
  }

  private SyntaxNode<Symbol, Token> functionNode(SyntaxNode<Symbol, Token> node) {
    val lexeme = node.getSource().getLexeme();
    val functionName = lexeme.substring(0, lexeme.length() - 1);
    val source = node.getSource();
    return new CssSyntaxNode(
        Symbol.symbol(functionName),
        new TokenWord(source.getStart(), source.getEnd(), functionName, source.getType()));
  }

  /**
   * parse a single attribute group
   *
   * @param tokens the current token-set
   * @param result the result to add the element to
   */
  private void parseAttributeGroup(
      LookaheadIterator<Token> tokens, List<SyntaxNode<Symbol, Token>> result) {
    val group = expect(tokens, AttributeGroup);
    eatWhitespace(tokens);
    val attribute = expect(tokens, Identifier);
    eatWhitespace(tokens);
    group.addChild(attribute);
    if (!nextIs(
        tokens,
        PrefixOperator,
        SuffixOperator,
        SubstringOperator,
        DashedPrefixOperator,
        StrictEqualityOperator,
        AttributeValueInSetOperator)) {
      expectAndDiscard(tokens, AttributeGroupEnd);
      result.add(group);
      return;
    }

    val operator =
        expect(
            tokens,
            PrefixOperator,
            SuffixOperator,
            SubstringOperator,
            DashedPrefixOperator,
            StrictEqualityOperator,
            AttributeValueInSetOperator);

    eatWhitespace(tokens);

    val operand = expect(tokens, Identifier, CssSelectorToken.String);

    group.addChildren(List.of(operator, operand));
    eatWhitespace(tokens);
    expectAndDiscard(tokens, AttributeGroupEnd);
    result.add(group);
  }

  /**
   * todo: add attrib, pseudo, negation
   *
   * @param tokens
   * @return
   */
  private boolean isSequence(LookaheadIterator<Token> tokens) {
    return nextIs(
        tokens,
        Identifier,
        Universal,
        IdentifierSelector,
        CssSelectorToken.Class,
        PseudoClass,
        PseudoElement,
        Not);
  }

  /**
   * determine if the next token is a combinator without advancing the stream
   *
   * @param tokens the token stream to check
   * @return true if the next token is a combinator
   */
  private boolean isCombinator(LookaheadIterator<Token> tokens) {
    return nextIs(tokens, Tilde, Whitespace, GreaterThan, AdditionOperator);
  }

  /**
   * utility method for determining whether the next element is in the target list
   *
   * @param tokens the token stream
   * @param match the set of elements to determine membership in
   * @return true if the next token is a member of the target set, false if not or the stream has no
   *     more tokens
   */
  private boolean nextIs(LookaheadIterator<Token> tokens, CssSelectorToken... match) {
    if (tokens.hasNext()) {
      val next = tokens.peek();
      val type = next.getType();
      for (val token : match) {
        if (type.equals(token)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * parse a combinator node
   *
   * @param tokens the token stream to extract a combinator from
   * @return the combinator
   * @throws IllegalArgumentException if the parser-state is in combinator position, but no known
   *     combinator exists
   */
  private SyntaxNode<Symbol, Token> combinator(LookaheadIterator<Token> tokens) {
    val token = tokens.peek();
    val type = (CssSelectorToken) token.getType();

    switch (type) {
      case Tilde:
      case Whitespace:
      case GreaterThan:
      case AdditionOperator:
        val result = new CssSyntaxNode(symbolForToken(type), tokens.next());
        eatWhitespace(tokens);
        return result;
      default:
        throw new IllegalArgumentException(
            format(
                "Unknown token in combinator position: %s at (%d,%d): %s",
                token.getType(), token.getStart(), token.getEnd(), token.getLexeme()));
    }
  }

  /**
   * expect a "union" operator (comma)
   *
   * @param tokens the token stream
   * @return a union operator
   * @throws IllegalArgumentException if the parser state is in union position, but the next
   *     operator is not a union operator
   */
  private SyntaxNode<Symbol, Token> union(LookaheadIterator<Token> tokens) {
    val next = tokens.peek();
    if (next.getType() != CssSelectorToken.Comma) {
      throw new IllegalArgumentException(
          format("Error: expected ',', got %s (%s)", next.getType(), next.getLexeme()));
    }
    val node = new CssSyntaxNode(ElementSymbol.Union, tokens.next());
    return node;
  }

  /**
   * consume until the next token is non-whitespace and place the iterator head there
   *
   * @param tokens the tokens stream
   */
  private void eatWhitespace(LookaheadIterator<Token> tokens) {
    while (tokens.hasNext()) {
      val next = tokens.next();
      if (next.getType() != Whitespace) {
        tokens.pushBack(next);
        break;
      }
    }
  }

  /**
   * determine if the next token in the stream is a member of the expected set, and, if it is return
   * a syntax node marking its placement in the abstract syntax tree
   *
   * @param tokens the token stream
   * @param types the set of tokens to expect such that the next element in the stream is in it
   * @return a syntax node backed by the token
   * @throws IllegalArgumentException if there is not a next token or the next token is not within
   *     the set
   */
  private SyntaxNode<Symbol, Token> expect(
      LookaheadIterator<Token> tokens, CssSelectorToken... types) {
    if (!tokens.hasNext()) {
      val expected = Arrays.stream(types).map(t -> t.name()).collect(Collectors.joining(","));
      throw new IllegalArgumentException(format("Expected one of [%s], got EOF", expected));
    }
    val next = tokens.peek();
    val nextType = (CssSelectorToken) next.getType();
    for (val type : types) {
      if (nextType.equals(type)) {
        return new CssSyntaxNode(symbolForToken(nextType), tokens.next());
      }
    }
    val expected = Arrays.stream(types).map(t -> t.name()).collect(Collectors.joining(","));
    throw new IllegalArgumentException(
        format("Expected one of [%s], got %s (%s)", expected, nextType, next));
  }

  /** the set of known CSS selector symbols, along with some that make processing the AST nicer */
  public enum ElementSymbol implements Symbol {
    /** a selector group. Individual CSS selector-sets are grouped beneath this for convenience */
    SelectorGroup("<group>", null),

    /** operators */

    /**
     * string value a string enclosed by either single-quotes ('), or double-quotes (") if the
     * string is single-quoted, then single-quotes must be escaped. Vice-versa for
     * double-quotes/single-quote escapes
     */
    StringValue("<string>", CssSelectorToken.String),

    /**
     * operator that matches a string prefix. Equivalent to: attribute.value.startsWith([operand])
     */
    PrefixMatch("^=", CssSelectorToken.PrefixOperator),

    /** operator that matches a string suffix. Equivalent to: attribute.value.endsWith([operand]) */
    SuffixMatch("$=", CssSelectorToken.SuffixOperator),

    /** operator that matches a substring. Equivalent to: attribute.value.contains([operand]) */
    SubstringMatch("*=", CssSelectorToken.SubstringOperator),

    /**
     * operator that matches a hyphen-separated list of values for instance, given an element e:
     *
     * <p>{@code <a lang="en_US-en_UK"></a> } a[lang|="en"] will match
     */
    DashMatch("|=", CssSelectorToken.DashedPrefixOperator),

    /**
     * includes is like dash-match only that, instead of a hyphen, it uses a space to delineat the
     * values
     *
     * <p>{@code <a lang="en whatever"> } will be matched by a[lang~=en]
     */
    Includes("~=", CssSelectorToken.AttributeValueInSetOperator),

    /** operator that matches a string exactly */
    StrictEquality("=", CssSelectorToken.StrictEqualityOperator),

    /** either an addition operator or an expression value, depending on state */
    Addition("+", AdditionOperator),

    /** subtraction operator */
    Subtraction("-", CssSelectorToken.Minus),

    /** numeric value--may be decimal or integral */
    Number("<number>", CssSelectorToken.Numeric),

    /** denotes the beginning of a selector call such as {@code :nth-child(2n + 1) } */
    FunctionApplication("<function>()", FunctionStart),
    /** pseudoclass */
    PseudoClass("::<class>", CssSelectorToken.PseudoClass),
    PseudoElement(":<element>", CssSelectorToken.PseudoElement),
    /** attribute selector */
    AttributeSelector("<attribute>", CssSelectorToken.AttributeGroup),
    /** a comma operator denotes set union a,b = union(select(a), select(b)) */
    Union(",", CssSelectorToken.Comma),

    /** the <code>not</code> operator negates enclosed operations */
    Negation(":not", CssSelectorToken.Not),

    /**
     * Universal selector (*) matches any nodes that are descendants of the current selector context
     * unless they are omitted by subsequent selectors
     */
    UniversalSelector("*", CssSelectorToken.Universal),
    /** select by node-type (h1, span, etc.) */
    TypeSelector("<type>", CssSelectorToken.Identifier),

    /** select by class (.red) */
    ClassSelector(".<identifier>", CssSelectorToken.Class),

    /** select children (parent {@code > } child) */
    ChildSelector(">", CssSelectorToken.GreaterThan),

    /** select by identity (#my-distinguished-node) */
    IdentitySelector("#<identifier>", CssSelectorToken.IdentifierSelector),

    /** selects any subsequent matching siblings (a ~ b) */
    GeneralSiblingSelector("~", Tilde),

    /** selects any immediately-subsequent matching siblings (a + b) */
    AdjacentSiblingSelector("+", CssSelectorToken.AdditionOperator),

    /** selects any descendants */
    DescendantSelector("<a desc b>", Whitespace);

    /** description */
    private final String value;

    /** the associated token */
    private final CssSelectorToken token;

    /**
     * constructor for the symbol
     *
     * @param value the string value
     * @param token the token, may be null
     */
    ElementSymbol(@Nonnull String value, @Nullable CssSelectorToken token) {
      this.value = value;
      this.token = token;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  public static final class CssSyntaxNode extends NamedSyntaxNode<Symbol, Token> {

    public CssSyntaxNode(Symbol symbol, Token token) {
      super(symbol.name(), symbol, token, symbol);
    }

    /**
     * useful for when there is no associated token
     *
     * @param symbol the symbol to associated with this node
     */
    private CssSyntaxNode(Symbol symbol) {
      this(symbol, null);
    }

    public String toString() {
      val content = getContent();

      return format("css[symbol:%s, name: %s]{%s}", getSymbol(), getName(), getSource());
    }
  }
}
