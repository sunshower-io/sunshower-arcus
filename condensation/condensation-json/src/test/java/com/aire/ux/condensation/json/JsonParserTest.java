package com.aire.ux.condensation.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.aire.ux.condensation.json.Value.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

public class JsonParserTest {

  @Test
  void ensureParsingArrayWithObjectValuesWorks() {
    val obj =
        "\n"
            + "[\n"
            + "  {\n"
            + "    \"firstName\": \"Josiah\",\n"
            + "    \"lastName\": \"Haswell\"\n"
            + "  },\n"
            + "  {\n"
            + "    \"firstName\": \"Lisa\",\n"
            + "    \"lastName\": \"Gumerman\"\n"
            + "  }\n"
            + "]";

    val ast = new JsonParser().parse(obj);
    assertEquals(ast.getRoot().getValue().getType(), Type.Array);
    System.out.println(ast);
  }

  @Test
  void ensureParsingArraysWorks() {
    val obj = "[1, 2, 3]";
    val ast = new JsonParser().parse(obj);
    assertEquals(ast.getRoot().getValue().getType(), Type.Array);
  }

  @Test
  void ensureParsingStringsWorks() {
    val obj = "{ \"hello\": \"world\" , \"howare\"  : \"you?\" }";
    val ast = new JsonParser().parse(obj);
    System.out.println(ast);
  }

  @Test
  void ensureParsingArrayOfObjectsWorks() {
    val str = read("test.json");
    val ast = new JsonParser().parse(str);
    System.out.println(ast);
  }

  @SneakyThrows
  public static String read(String s) {
    val resource = ClassLoader.getSystemClassLoader().getResource(s);
    if (resource == null) {
      throw new NoSuchElementException("No resource: " + s);
    }
    return Files.readString(Path.of(resource.toURI()));
  }
}
