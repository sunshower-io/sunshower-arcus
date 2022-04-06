package com.aire.ux.select.css;

import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.ast.core.TokenBuffer;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.concurrent.NotThreadSafe;

/** wraps CssSelectorToken in a Lexer. */
@NotThreadSafe
public class DefaultCssSelectorLexer implements SelectorLexer {

  final TokenBuffer buffer;

  public DefaultCssSelectorLexer() {
    buffer = CssSelectorToken.createTokenBuffer();
  }

  @Override
  public Stream<Token> stream(@Nonnull CharSequence seq) {
    return buffer.stream(seq);
  }

  @Override
  public Iterable<Token> lex(@Nonnull CharSequence seq) {
    return buffer.tokenize(seq);
  }
}
