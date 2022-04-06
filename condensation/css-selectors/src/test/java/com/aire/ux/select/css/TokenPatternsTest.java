package com.aire.ux.select.css;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;
import lombok.val;
import org.junit.jupiter.api.Test;

class TokenPatternsTest {

  @Test
  void ensureIdentifierMatchesName() {

    val pattern = Pattern.compile(TokenPatterns.IDENTIFIER).matcher("hello-world");
    assertTrue(pattern.matches());
  }

  @Test
  void ensureNamePatternMatchesSimpleCharacter() {
    val pattern = Pattern.compile(TokenPatterns.NAME_START).matcher("p");
    assertTrue(pattern.matches());
  }

  @Test
  void ensureStringForm1Works() {
    val pattern = Pattern.compile(TokenPatterns.STRING_FORM_1).matcher("\"hello  \\\" World\"");
    assertTrue(pattern.matches());
  }

  @Test
  void ensureStringForm2Works() {
    val pattern = Pattern.compile(TokenPatterns.STRING_FORM_1).matcher("\"hello  \\' World\"");
    assertTrue(pattern.matches());
  }
}
