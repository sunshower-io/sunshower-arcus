package io.sunshower.arcus.ast.core;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.Objects;
import lombok.val;

/** idk why lombok isn't recognizing generated getters */
@SuppressFBWarnings
public final class TokenWord implements Token {

  final int end;
  final int start;
  final Type type;
  final String lexeme;

  public TokenWord(int start, int end, String lexeme, Type type) {
    this.end = end;
    this.type = type;
    this.start = start;
    this.lexeme = lexeme;
  }

  @Override
  public String getLexeme() {
    return lexeme;
  }

  @Override
  public int getEnd() {
    return end;
  }

  @Override
  public int getStart() {
    return start;
  }

  @Override
  public Token setType(Type type) {
    return new TokenWord(start, end, lexeme, type);
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public int hashCode() {
    return Objects.hash(start, end, type, lexeme);
  }

  @Override
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (o == null) {
      return false;
    }
    if (o instanceof TokenWord) {
      val tw = (TokenWord) o;
      return start == tw.start
          && end == tw.end
          && type == tw.type
          && Objects.equals(lexeme, tw.lexeme);
    }
    return false;
  }
}
