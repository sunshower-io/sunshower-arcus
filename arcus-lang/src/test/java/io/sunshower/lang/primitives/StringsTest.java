package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.Test;

public class StringsTest {

  @Test
  void ensureFastIndexOfWorksOnSeq() {
    val search = "hello";
    val string = "adfafadfadfasdfhelloworld";

    val result = Strings.indexOf(string, search);
    assertEquals(string.lastIndexOf("hello"), result);
  }


  @Test
  void ensureFastIndexOfWorks() {
    val search = "hello".toCharArray();
    val string = "adfafadfadfasdfhelloworld";

    val result = Strings.indexOf(string.toCharArray(), search);
    assertEquals(string.lastIndexOf("hello"), result);
  }

  @Test
  void ensureEmptyStringIsEmpty() {
    assertEquals(Strings.isBlank(""), true);
  }
}
