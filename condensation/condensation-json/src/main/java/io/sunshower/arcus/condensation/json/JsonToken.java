package io.sunshower.arcus.condensation.json;

import static java.lang.String.format;

import io.sunshower.arcus.ast.Symbol;
import io.sunshower.arcus.ast.core.TokenBuffer;
import io.sunshower.arcus.ast.core.Type;
import io.sunshower.arcus.condensation.mappings.LRUCache;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
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

  private final Map<CharSequence, JsonToken> internmap = new LRUCache<>(15);

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
  public Type getMatching(CharSequence toMatch) {

    val r = internmap.get(toMatch);
    if(r != null) {
      internmap.put(toMatch, r);
      return r;
    }
    return Type.super.getMatching(toMatch);
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
