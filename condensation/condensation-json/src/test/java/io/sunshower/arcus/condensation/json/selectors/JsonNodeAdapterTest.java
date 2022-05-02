package io.sunshower.arcus.condensation.json.selectors;

import static com.aire.ux.condensation.json.JsonParserTest.read;
import static com.aire.ux.condensation.json.JsonParserTest.readStream;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.condensation.json.JsonParser;
import io.sunshower.arcus.selectors.css.CssSelectorParser;
import io.sunshower.arcus.selectors.plan.DefaultPlanContext;
import lombok.val;
import org.junit.jupiter.api.Test;

class JsonNodeAdapterTest {

  @Test
  void ensureSelectingByTypeWorks() {
    val node = new JsonParser().parse(read("test.json"));
    val parser = new CssSelectorParser().parse(".world > .numarray .cool");
    val results =
        parser
            .plan(DefaultPlanContext.getInstance())
            .evaluate(node.getRoot(), new JsonNodeAdapter());
    System.out.println(results);
  }

  @Test
  void ensureQueryingFriendsWorks() {}

  @Test
  void ensureASTsAreEqualForLargeDocument() {
    val parser = new JsonParser();
    val document = readStream("test.json");
    val docString = read("test.json");
    val result = parser.parse(document);
    assertEquals(parser.parse(docString), result);
  }

  @Test
  void ensureParsingLargeFileWorksFromInputStreamWorks() {
    long average = 0;
    val parser = new JsonParser();
    for (int i = 0; i < 100; i++) {
      val document = readStream("large.json");
      long fst = System.currentTimeMillis();
      val result = parser.parse(document);
      long snd = System.currentTimeMillis();
      val time = (snd - fst);
      System.out.println("Time: " + time);
      average = (average + (snd - fst));
      System.out.println(average);
    }
    System.out.println("Average: " + average / 100);
  }

  @Test
  void ensureParsingLargeFileWorks() {
    long average = 0;
    val document = read("large.json");
    val parser = new JsonParser();
    for (int i = 0; i < 100; i++) {
      long fst = System.currentTimeMillis();
      val result = parser.parse(document);
      long snd = System.currentTimeMillis();
      val time = (snd - fst);
      System.out.println("Time: " + time);
      average = (average + (snd - fst));
      System.out.println(average);
    }
    System.out.println("Average: " + average / 100);
    System.out.println(parser.parse(document));
  }
}
