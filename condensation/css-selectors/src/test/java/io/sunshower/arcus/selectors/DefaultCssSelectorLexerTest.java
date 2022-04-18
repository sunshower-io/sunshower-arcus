package io.sunshower.arcus.selectors;

import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.arcus.selectors.css.DefaultCssSelectorLexer;
import lombok.val;
import org.junit.jupiter.api.Test;

class DefaultCssSelectorLexerTest {

  @Test
  void ensureLexerReadsInclusionMatcherAndTildeCorrectly() {
    val results = new DefaultCssSelectorLexer().lex("~ ~=");
    val iter = results.iterator();
    assertEquals("~", iter.next().getLexeme());
    assertEquals(" ", iter.next().getLexeme());
    assertEquals("~=", iter.next().getLexeme());
  }
}
