package io.sunshower.arcus.markup;

import static io.sunshower.arcus.markup.Tags.tag;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.markup.writers.XmlWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.val;
import org.junit.jupiter.api.Test;

class DefaultTagTest {

  @Test
  void ensureContentIsExpected() throws IOException {
    val result =
        Tags.root("hello")
            .content("this is the best \n" + "content \n" + "don't you think?")
            .child(tag("child1").attribute("first", "second"))
            .children(
                tag("child2").attribute("first", "second"),
                tag("child3")
                    .attribute("first", "second")
                    .attribute("third", "fourth")
                    .content(
                        "hello world how are you?\nthis is pretty\n\n\t\n good \n content \n don't you think?\n"));

    val outputStream = new ByteArrayOutputStream();
    val output = new XmlWriter(outputStream);
    result.write(output);
    outputStream.flush();

    val r = outputStream.toString(StandardCharsets.UTF_8);

    assertEquals(
        "<hello\n"
            + "> this is the best \n"
            + " content \n"
            + " don't you think?\n"
            + " <child1\n"
            + "  first=\"second\"\n"
            + " > </child1>\n"
            + " <child2\n"
            + "  first=\"second\"\n"
            + " > </child2>\n"
            + " <child3\n"
            + "  first=\"second\"\n"
            + "  third=\"fourth\"\n"
            + " >  hello world how are you?\n"
            + "  this is pretty\n"
            + "  \n"
            + "  \t\n"
            + "   good \n"
            + "   content \n"
            + "   don't you think?\n"
            + " </child3>\n"
            + "</hello>",
        r.trim());
  }
}
