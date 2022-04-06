package com.aire.ux.condensation.json;

import static com.aire.ux.condensation.json.JsonParserTest.read;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.aire.ux.condensation.Condensation;
import io.sunshower.arcus.ast.core.Token;
import io.sunshower.arcus.ast.core.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JsonTokenTest {

  private static void expect(CharSequence value, Type... types) {
    val actualTokens = tokenize(value).iterator();
    for (val tokenType : types) {
      if (!actualTokens.hasNext()) {
        fail(String.format("Expected token '%s' but none remained in the buffer", tokenType));
      } else {
        assertEquals(tokenType, actualTokens.next().getType());
      }
    }
    if (actualTokens.hasNext()) {
      val stream =
          StreamSupport.stream(
                  Spliterators.spliteratorUnknownSize(actualTokens, Spliterator.ORDERED), false)
              .map(t -> t.getType().toString());
      fail(
          String.format(
              "Expected no more tokens, got '%s'", stream.collect(Collectors.joining(","))));
    }
  }

  private static Iterable<Token> tokenize(CharSequence value) {
    return JsonToken.createTokenBuffer().tokenize(value);
  }

  @Test
  void ensureOpenBracketIsRecognized() {
    expect("{", JsonToken.OpenBrace, "{");
  }

  @Test
  void ensureClosingBracketIsRecognized() {
    expect("}", JsonToken.CloseBrace, "}");
  }

  @Test
  void ensureBracketsAreTokenized() {
    expect("{}", JsonToken.OpenBrace, JsonToken.CloseBrace);
  }

  @ParameterizedTest
  @ValueSource(strings = {" ", "\n", "\r", "\f", " \n \r \f   "})
  void ensureWhitespaceIsTokenized(String value) {
    expect(value, JsonToken.WhiteSpace);
  }

  @ParameterizedTest
  @ValueSource(strings = {"{ }", "{  }", "{\n " + "\n}"})
  void ensureEmptyObjectLiteralIsParsedCorrectly(String value) {
    expect(value, JsonToken.OpenBrace, JsonToken.WhiteSpace, JsonToken.CloseBrace);
  }

  @ParameterizedTest
  @ValueSource(strings = {"{\"\"}", "{\"\\\"\"}", "{\"h{el}lo\"}"})
  void ensureLexerHandlesStringsCorrectly(String value) {
    expect(value, JsonToken.OpenBrace, JsonToken.String, JsonToken.CloseBrace);
  }

  @ParameterizedTest
  @ValueSource(strings = "[]")
  void ensureArrayBracketsWork(String value) {
    expect(value, JsonToken.ArrayOpen, JsonToken.ArrayClose);
  }

  @Test
  void ensureGeneralSiblingWorks() {
    val doc =
        Condensation.parse(
            "json",
            "     {\n"
                + "      \"hello\": 1,\n"
                + "        \"world\": 2,\n"
                + "        \"stuff\": 3\n"
                + "    }\n ");
    val result = new HashSet<>(doc.selectAll(".hello ~ number"));
    assertEquals(result, Set.of(2.0, 3.0));
  }

  @Test
  void ensureImmediateSiblingWorks() {
    val doc =
        Condensation.parse(
            "json",
            "     {\n"
                + "      \"hello\": 1,\n"
                + "        \"world\": 2,\n"
                + "        \"stuff\": 3\n"
                + "    }\n ");
    val result = new HashSet<>(doc.selectAll(".hello + number"));
    assertEquals(result, Set.of(2.0));
  }

  @Test
  void printAll() {
    System.out.println(new JsonParser().parse(read("test.json")));
  }

  @Test
  @SneakyThrows
  @Disabled("Testing a huge file that we don't want to commit into github")
  void ensureReadingHugeFileWorks() {
    val contents = Files.readString(Path.of("/home/josiah/Downloads/citylots.json"));
    long l1 = System.currentTimeMillis();
    try {
      new JsonParser().parse(contents);
    } catch (Throwable t) {
      t.printStackTrace();
    }
    long l2 = System.currentTimeMillis();
    System.out.println("Completed parsing in: " + (l2 - l1) / 1000 + " seconds");
  }

  @Test
  void ensureNthChildSelectorWorks() {
    val doc =
        Condensation.parse(
            "json",
            "  {\n"
                + "          \"whatever\": [\n"
                + "            1,\n"
                + "            2,\n"
                + "            3,\n"
                + "            4,\n"
                + "            5,\n"
                + "            6,\n"
                + "            -11431345e-142,\n"
                + "            11431345e-142\n"
                + "          ]\n"
                + "        }");
    val result = new LinkedHashSet<>(doc.selectAll("string > number:nth-child(odd)"));
    assertEquals(Set.of(1.0, 3.0, 5.0, -1.1431345E-135), result);
  }

  private void expect(String expr, JsonToken token, String lexeme) {
    val matcher = token.getPattern().matcher(expr);
    assertTrue(matcher.matches());
    val group = matcher.group(token.name());
    assertEquals(group, lexeme);
  }
}
