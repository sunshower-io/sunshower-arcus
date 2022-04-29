package io.sunshower.arcus.ast.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;
import lombok.val;

public interface Type {


  default Type getMatching(CharSequence toMatch) {
    for(val t : enumerate()) {
      if(t.matcher(toMatch).matches()) {
        return t;
      }
    }
    throw new IllegalArgumentException(String.format("No matching type for '%s'", toMatch));
  }

  /**
   * @param <T> this type
   * @return the list of all possible values of this type
   */
  <T extends Type> Iterable<T> enumerate();

  /** @return the name of this type */
  @Nonnull
  String name();

  @Nonnull
  default Matcher matcher(@Nonnull CharSequence sequence) {
    return getPattern().matcher(sequence);
  }

  /** @return the pattern backing this token type */
  @Nonnull
  Pattern getPattern();
}
