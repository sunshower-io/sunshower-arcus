package com.aire.ux.condensation.json;

import static com.aire.ux.condensation.json.JsonParserTest.read;
import static org.junit.jupiter.api.Assertions.*;

import com.aire.ux.condensation.Condensation;
import lombok.val;
import org.junit.jupiter.api.Test;

class JsonParserFactoryTest {

  @Test
  void ensureParsingDocumentWorks() {
    val document = Condensation.parse("json", read("test.json"));
    val value = document.select(".world > .five");
    assertEquals(value, 5d);
  }

  @Test
  void ensureChildSelectorWorks() {
    val document = Condensation.parse("json", read("test.json"));
    val value = document.selectAll(".world > .numarray > number");
    System.out.println(value);
  }

  @Test
  void ensureGeneralSiblingSelectorWorks() {
    val document = Condensation.parse("json", read("test.json"));
    val value =
        document.selectAll(
            ".world > .numarray > number:nth-child(1), .world > number:nth-child(odd), number:nth-child(even)");
    System.out.println(value);
  }

  @Test
  void ensureStringsWorks() {
    val document = Condensation.parse("json", read("test.json"));
    val value = document.selectAll("string");
    System.out.println(value);
  }
}
