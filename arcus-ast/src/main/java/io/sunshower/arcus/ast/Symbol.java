package io.sunshower.arcus.ast;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

/** marker for a parsed symbol. Probably best to not overload Symbols and Tokens */
@SuppressWarnings("PMD")
public interface Symbol {

  /**
   * lookup or create a symbol
   *
   * @param value the underlying value
   * @return the symbol
   */
  static Symbol symbol(@Nonnull String value) {
    return StringSymbol.internmap.computeIfAbsent(value, StringSymbol::new);
  }

  /** @return the name for this symbol */
  default String name() {
    return getClass().getSimpleName();
  }
}

@SuppressFBWarnings
@SuppressWarnings("PMD")
final class StringSymbol implements Symbol {

  final String symbol;

  StringSymbol(@Nonnull final String symbol) {
    this.symbol = symbol;
  }

  static final Map<String, Symbol> internmap;

  static {
    internmap = new HashMap<>();
  }

  @Override
  public String name() {
    return symbol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof StringSymbol)) {
      return false;
    }

    StringSymbol that = (StringSymbol) o;

    return Objects.equals(symbol, that.symbol);
  }

  @Override
  public int hashCode() {
    return symbol != null ? symbol.hashCode() : 0;
  }
}
