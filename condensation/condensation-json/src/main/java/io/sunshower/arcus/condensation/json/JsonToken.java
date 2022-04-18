package io.sunshower.arcus.condensation.json;

import static java.lang.String.format;

import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.core.TokenBuffer;
import io.sunshower.arcus.ast.core.Type;
import java.util.Arrays;
import java.util.regex.Pattern;
import lombok.NonNull;
import lombok.val;

public enum JsonToken implements Type, Symbol {
  Colon(":"),
  Comma(","),
  Null("null"),
  Boolean("true|false"),
  String("\"[^\"\\\\]*(\\\\.[^\"\\\\]*)*\""),
  Number("([-+]?\\d*\\.?\\d+)(?:[eE]([-+]?\\d+))?"),
  OpenBrace("\\{"),
  CloseBrace("}"),
  ArrayOpen("\\["),
  ArrayClose("\\]"),
  WhiteSpace("[ \n\r\f\b  ]+");

  private final String pattern;
  private final boolean include;

  private volatile Pattern cachedPattern;

  JsonToken(String pattern) {
    this(pattern, true);
  }

  JsonToken(@NonNull String pattern, boolean include) {
    this.pattern = pattern;
    this.include = include;
  }

  @NonNull
  public static TokenBuffer createTokenBuffer() {
    val buffer = new StringBuilder();
    for (val token : values()) {
      if (token.include) {
        bufferTokenPattern(buffer, token);
      }
    }
    return new TokenBuffer(JsonToken.OpenBrace, buffer);
  }

  private static void bufferTokenPattern(StringBuilder buffer, JsonToken token) {
    buffer.append(format("|(?<%s>%s)", token.name(), token.pattern));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Type> Iterable<T> enumerate() {
    return () -> Arrays.stream(JsonToken.values()).map(t -> (T) t).iterator();
  }

  @NonNull
  @Override
  public Pattern getPattern() {
    var result = cachedPattern;
    if (result == null) {
      synchronized (this) {
        result = cachedPattern;
        if (result == null) {
          result = cachedPattern = Pattern.compile(format("(?<%s>%s)", name(), pattern));
        }
      }
    }
    return result;
  }
}
