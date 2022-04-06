package com.aire.ux.condensation.json;

import static io.sunshower.arcus.ast.core.LookaheadIterator.wrap;

import io.sunshower.arcus.ast.AbstractSyntaxNode;
import io.sunshower.arcus.ast.AbstractSyntaxTree;
import io.sunshower.arcus.ast.SyntaxNode;
import io.sunshower.arcus.ast.core.LookaheadIterator;
import io.sunshower.arcus.ast.core.Token;
import java.util.NoSuchElementException;
import lombok.val;

@SuppressWarnings("PMD.CompareObjectsWithEquals")
public class JsonParser {

  public AbstractSyntaxTree<Value<?>, Token> parse(CharSequence sequence) {
    val tokens = tokenize(sequence);
    return new AbstractSyntaxTree<>(json(tokens));
  }

  private SyntaxNode<Value<?>, Token> json(LookaheadIterator<Token> tokens) {
    return element(tokens);
  }

  private SyntaxNode<Value<?>, Token> element(LookaheadIterator<Token> tokens) {
    if (!tokens.hasNext()) {
      throw new NoSuchElementException("Nope"); // todo make useful
    }
    whitespace(tokens);
    val value = value(tokens);
    whitespace(tokens);
    return value;
  }

  private SyntaxNode<Value<?>, Token> value(LookaheadIterator<Token> tokens) {
    val next = tokens.peek();
    val nextType = (JsonToken) next.getType();
    switch (nextType) {
      case String:
        return string(tokens);
      case OpenBrace:
        val object = object(tokens);
        expect(tokens, JsonToken.CloseBrace);
        return object;
      case ArrayOpen:
        val array = array(tokens);
        expect(tokens, JsonToken.ArrayClose);
        return array;
      case Number:
        return number(tokens);
      case Boolean:
        val booleanToken = expect(tokens, JsonToken.Boolean);
        return new JsonSyntaxNode(booleanToken, Values.bool(booleanToken.getLexeme()));
      case Null:
        val nullToken = expect(tokens, JsonToken.Null);
        return new JsonSyntaxNode(nullToken, Values.nullValue());
    }
    throw new ParsingException(next);
  }

  private SyntaxNode<Value<?>, Token> number(LookaheadIterator<Token> tokens) {
    val token = tokens.next();
    return new JsonSyntaxNode(token, Values.number(token.getLexeme()));
  }

  private SyntaxNode<Value<?>, Token> array(LookaheadIterator<Token> tokens) {
    val value = Values.array();
    val arrayToken = expect(tokens, JsonToken.ArrayOpen);
    val arrayNode = new JsonSyntaxNode(arrayToken, value);

    for (; ; ) {
      whitespace(tokens);
      if (peekType(tokens, JsonToken.ArrayClose)) {
        return arrayNode;
      }
      val arrayElement = element(tokens);
      arrayNode.addChild(arrayElement);
      value.add(arrayElement.getValue());
      whitespace(tokens);
      if (peekType(tokens, JsonToken.ArrayClose)) {
        return arrayNode;
      }
      expect(tokens, JsonToken.Comma);
    }
  }

  private SyntaxNode<Value<?>, Token> object(LookaheadIterator<Token> tokens) {
    val value = Values.object();
    val objectToken = expect(tokens, JsonToken.OpenBrace);
    val objectNode = new JsonSyntaxNode(objectToken, value);
    for (; ; ) {
      whitespace(tokens);
      if (peekType(tokens, JsonToken.CloseBrace)) {
        return objectNode;
      }
      val memberName = string(tokens);
      whitespace(tokens);
      expect(tokens, JsonToken.Colon);
      val memberValue = element(tokens);
      memberName.addChild(memberValue);
      objectNode.addChild(memberName);
      value.set((String) memberName.getValue().getValue(), memberValue.getValue());
      whitespace(tokens);
      if (peekType(tokens, JsonToken.CloseBrace)) {
        return objectNode;
      }
      expect(tokens, JsonToken.Comma);
    }
  }

  private boolean peekType(LookaheadIterator<Token> tokens, JsonToken type) {
    return tokens.hasNext() && tokens.peek().getType() == type;
  }

  private SyntaxNode<Value<?>, Token> string(LookaheadIterator<Token> tokens) {
    val value = expect(tokens, JsonToken.String);
    return new JsonSyntaxNode(value, Values.string(value));
  }

  private Token expect(LookaheadIterator<Token> tokens, JsonToken... expected) {
    if (!tokens.hasNext()) {
      throw new ParsingException(tokens, expected);
    }
    val next = tokens.next();
    val nextType = next.getType();
    for (val expectation : expected) {
      if (nextType == expectation) {
        return next;
      }
    }
    throw new ParsingException(tokens, next, expected);
  }

  private void whitespace(LookaheadIterator<Token> tokens) {
    while (tokens.hasNext()) {
      val next = tokens.next();
      if (next.getType() != JsonToken.WhiteSpace) {
        tokens.pushBack(next);
        return;
      }
    }
  }

  private LookaheadIterator<Token> tokenize(CharSequence sequence) {
    return wrap(JsonToken.createTokenBuffer().tokenize(sequence).iterator());
  }

  static final class JsonSyntaxNode extends AbstractSyntaxNode<Value<?>, Token> {

    public JsonSyntaxNode(Token source, Value value) {
      super((JsonToken) source.getType(), source, value);
    }
  }
}
