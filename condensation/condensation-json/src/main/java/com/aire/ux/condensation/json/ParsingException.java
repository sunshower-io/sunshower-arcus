package com.aire.ux.condensation.json;

import io.sunshower.arcus.ast.core.LookaheadIterator;
import io.sunshower.arcus.ast.core.Token;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ParsingException extends RuntimeException {

  public ParsingException(LookaheadIterator<Token> tokens, JsonToken... expected) {}

  public ParsingException(LookaheadIterator<Token> tokens, Token actual, JsonToken... expected) {
    super(messageFor(tokens, actual, expected));
  }

  public ParsingException(Token unexpected) {
    super(messageForUnexpectedToken(unexpected));
  }

  private static String messageForUnexpectedToken(Token unexpected) {
    return String.format(
        "Unexpected token: '%s' (type: %s) at (%d, %d)",
        unexpected.getLexeme(), unexpected.getType(), unexpected.getStart(), unexpected.getEnd());
  }

  static String messageFor(LookaheadIterator<Token> tokens, Token actual, JsonToken... expected) {
    return String.format(
        "Expected '%s' at (%d, %d), got '%s'",
        actual.getLexeme(),
        actual.getStart(),
        actual.getEnd(),
        Arrays.stream(expected).map(Enum::name).collect(Collectors.joining(",")));
  }
}
