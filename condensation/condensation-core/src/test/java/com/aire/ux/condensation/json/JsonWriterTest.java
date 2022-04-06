package com.aire.ux.condensation.json;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.aire.ux.condensation.Alias;
import com.aire.ux.condensation.Attribute;
import com.aire.ux.condensation.Condensation;
import com.aire.ux.condensation.Element;
import com.aire.ux.condensation.PropertyScanner;
import com.aire.ux.condensation.RootElement;
import com.aire.ux.condensation.mappings.AnnotationDrivenPropertyScanner;
import com.aire.ux.condensation.mappings.CachingDelegatingTypeInstantiator;
import com.aire.ux.condensation.mappings.DefaultTypeBinder;
import com.aire.ux.condensation.mappings.ReflectiveTypeInstantiator;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonWriterTest {

  private PropertyScanner scanner;
  private DefaultTypeBinder binder;
  private ReflectiveTypeInstantiator instantiator;

  @BeforeEach
  void setUp() {
    instantiator = new ReflectiveTypeInstantiator();
    scanner =
        new AnnotationDrivenPropertyScanner(new CachingDelegatingTypeInstantiator(instantiator));
    binder = new DefaultTypeBinder(scanner);
  }

  @Test
  void ensureObtainingWriterWorks() {
    val writer = Condensation.create("json").getWriter();
    assertNotNull(writer, "writer must not be null");
  }

  @Test
  void ensureWritingDocumentWorks() throws IOException {

    @RootElement
    class A {

      @Element private String a;
    }
    val a = new A();
    a.a = "hello-world";
    val writer = new ByteArrayOutputStream();
    Condensation.create("json").getWriter().write(A.class, a, writer);
    assertResultEquals(writer, "{\"a\":\"hello-world\"}");
  }

  @Test
  void ensureWritingDocumentWorks_int_property() throws IOException {

    @RootElement
    class A {

      @Element private int a;
    }
    val a = new A();
    a.a = 1;
    val writer = new ByteArrayOutputStream();
    Condensation.create("json").getWriter().write(A.class, a, writer);
    assertResultEquals(writer, "{\"a\":1}");
  }

  @Test
  void ensureWritingNestedValueWorks() throws IOException {

    @RootElement
    class A {

      @Attribute String value;

      @Element private A a;
    }
    val a = new A();
    a.a = new A();
    a.a.value = "coolbeans";
    val writer = new ByteArrayOutputStream();
    Condensation.create("json").getWriter().write(A.class, a, writer);

    //    assertResultEquals(writer, "{\"a\":{\"a\":null,\"value\":\"coolbeans\"},\"value\":null}");
  }

  @Test
  void ensureWritingArrayWorks() throws IOException {
    @RootElement
    @EqualsAndHashCode
    class MyType {

      @Element String hello;

      @Element(alias = @Alias(write = "sup", read = "sup"))
      String world;
    }
    val results = new MyType[100];
    for (int i = 0; i < 100; i++) {
      val t = new MyType();
      t.hello = "hello-" + i;
      t.world = "world-" + i;
      results[i] = t;
    }

    val writer = new ByteArrayOutputStream();
    Condensation.create("json").getWriter().write(MyType[].class, results, writer);
    instantiator.register(MyType.class, MyType::new);
    System.out.println(writer.toString());
    val read = Condensation.read(MyType[].class, "json", writer.toString(), binder);
    assertArrayEquals(results, read);
  }

  @Test
  void ensureWritingCollectionWorks() throws IOException {

    @RootElement
    @EqualsAndHashCode
    class MyType {

      @Element String hello;

      @Element(alias = @Alias(write = "sup", read = "sup"))
      String world;
    }
    val results = new ArrayList<MyType>();
    for (int i = 0; i < 100; i++) {
      val t = new MyType();
      t.hello = "hello-" + i;
      t.world = "world-" + i;
      results.add(t);
    }

    val writer = new ByteArrayOutputStream();
    Condensation.create("json").getWriter().write(List.class, results, writer);
    instantiator.register(MyType.class, MyType::new);
    System.out.println(writer.toString());
    val read = List.of(Condensation.read(MyType[].class, "json", writer.toString(), binder));
    assertEquals(results, read);
  }

  @Test
  void ensureWritingMapWorks() throws IOException {

    @RootElement
    @EqualsAndHashCode
    class MyType {

      @Element String hello;

      @Element(alias = @Alias(write = "sup", read = "sup"))
      String world;

      @Element Map<String, List<MyType>> values;
    }
    val results = new ArrayList<MyType>();
    for (int i = 0; i < 100; i++) {
      val t = new MyType();
      t.hello = "hello-" + i;
      t.world = "world-" + i;
      results.add(t);
    }

    val m = new HashMap<String, MyType>();
    for (val t : results) {
      m.put(t.hello, t);
      t.values = new HashMap<>();
      t.values.put(
          "values",
          results.stream()
              .map(
                  result -> {
                    result.values = new HashMap<>();
                    val r = result.world;
                    result.world = result.hello;
                    result.hello = r;
                    val copy = new MyType();
                    copy.hello = r;
                    copy.world = result.hello;
                    result.values.put("value", List.of(copy));
                    return result;
                  })
              .collect(Collectors.toList()));
    }

    val writer = new ByteArrayOutputStream();
    Condensation.create("json").getWriter().write(Map.class, m, writer);
    instantiator.register(MyType.class, MyType::new);
    System.out.println(writer.toString());
  }

  private void assertResultEquals(ByteArrayOutputStream writer, String s) {
    assertEquals(writer.toString(StandardCharsets.UTF_8), s);
  }
}
