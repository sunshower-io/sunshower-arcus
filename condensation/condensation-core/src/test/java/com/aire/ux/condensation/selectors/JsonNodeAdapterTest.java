package com.aire.ux.condensation.selectors;

import static com.aire.ux.condensation.json.JsonParserTest.read;

import com.aire.ux.condensation.json.JsonParser;
import com.aire.ux.plan.DefaultPlanContext;
import com.aire.ux.select.css.CssSelectorParser;
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
