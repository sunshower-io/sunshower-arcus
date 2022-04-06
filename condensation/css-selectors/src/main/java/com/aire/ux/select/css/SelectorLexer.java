package com.aire.ux.select.css;

import io.sunshower.arcus.ast.core.Token;
import java.util.stream.Stream;
import javax.annotation.Nonnull;

public interface SelectorLexer {

  /**
   * @param seq the character sequence to scan
   * @return an interable over the scanned tokens. This should be lazy
   */
  @Nonnull
  Stream<Token> stream(@Nonnull CharSequence seq);

  /**
   * @param seq the character sequence to scan
   * @return an iterable over the scanned tokens. This should be lazy
   */
  @Nonnull
  Iterable<Token> lex(@Nonnull CharSequence seq);
}
