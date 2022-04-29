package io.sunshower.arcus.ast.core;

import java.io.InputStream;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javax.annotation.concurrent.NotThreadSafe;
import lombok.val;

@NotThreadSafe
public final class TokenBuffer {

  private final Type type;
  private final Pattern patternBuffer;

  /**
   * @param buffer the buffer to build this from note that the first character is '|' so we'll strip
   *               that
   */
  public TokenBuffer(final Type type, final StringBuilder buffer) {
    this.type = type;
    this.patternBuffer = Pattern.compile(buffer.substring(1), Pattern.CASE_INSENSITIVE);
  }

  /**
   * @param seq the sequence to scan
   * @return a stream of the tokens
   */
  public Stream<Token> stream(CharSequence seq) {
    return StreamSupport.stream(
        Spliterators.spliteratorUnknownSize(tokenize(seq).iterator(), Spliterator.ORDERED), false);
  }

  public Iterable<Token> tokenize(InputStream inputStream) {
    return () -> new Scanner(inputStream).findAll(patternBuffer)
        .map(matchResult -> (Token) new TokenWord(
            matchResult.start(),
            matchResult.end(),
            matchResult.group(), type.getMatching(matchResult.group()))).iterator();
  }

  public Iterable<Token> tokenize(CharSequence sequence) {
    val matcher = patternBuffer.matcher(sequence);
    return () ->
        new Iterator<>() {
          boolean matchAttempted = false;

          @Override
          public boolean hasNext() {
            if (!matchAttempted) {
              return (matchAttempted = matcher.find());
            }
            return true;
          }

          @Override
          public Token next() {
            if (!matchAttempted) {
              if (!hasNext()) {
                throw new NoSuchElementException("EOF");
              }
            }

            matchAttempted = false;
            for (val token : type.enumerate()) {
              val group = matcher.group(token.name());
              if (group != null) {
                val start = matcher.start(token.name());
                val end = matcher.end(token.name());
                return new TokenWord(start, end, group, token);
              }
            }
            return null;
          }
        };
  }

}
