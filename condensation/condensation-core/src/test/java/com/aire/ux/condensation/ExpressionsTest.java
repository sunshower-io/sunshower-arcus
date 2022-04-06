package com.aire.ux.condensation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import lombok.val;
import org.junit.jupiter.api.RepeatedTest;

public class ExpressionsTest {

  static Condensation condensation = Condensation.create("json");

  @RepeatedTest(1000)
  void ensureSubstitutingComplexObjectWorks() throws IOException {

    @RootElement
    class Descendant {

      @Element(alias = @Alias(write = "my-name"))
      String name;
    }
    @RootElement
    class Test {

      @Element(alias = @Alias(write = "my-name"))
      String name;

      @Element Descendant descendant;
    }

    val test = new Test();
    test.name = "hello";
    val desc = new Descendant();
    desc.name = "world";
    test.descendant = desc;
    val os = new ByteArrayOutputStream();
    condensation.getWriter().write(Test.class, test, os);
    val result = MessageFormat.format("hello {0}", os.toString(StandardCharsets.UTF_8));
    assertEquals("hello {\"my-name\":\"hello\",\"descendant\":{\"my-name\":\"world\"}}", result);
  }
}
