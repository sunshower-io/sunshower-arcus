package com.aire.ux.condensation.json;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.condensation.Alias;
import io.sunshower.arcus.condensation.Attribute;
import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.arcus.condensation.Convert;
import io.sunshower.arcus.condensation.Converter;
import io.sunshower.arcus.condensation.Element;
import io.sunshower.arcus.condensation.RootElement;
import io.sunshower.arcus.condensation.mappings.ReflectiveTypeInstantiator;
import io.sunshower.arcus.reflect.Reflect;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CondensationTest {


  private Condensation condensation;

  @BeforeEach
  void setUp() {
    condensation = Condensation.create("json");
  }


  @Test
  @SneakyThrows
  void ensureReadingSimpleClassFromInputStreamWorksArrayWorks() {

    @RootElement
    class Value {

      @Attribute
      String name;
    }

    val source = """
        [{
          "name": "Josiah"
        },
        {
          "name": "Lisa"
        }
        ]
        """;
    register(Value.class, Value::new);
    val v = condensation.readAll(Value.class,
        ArrayList::new,
        new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));
    assertEquals(2, v.size());

    assertEquals(v.get(0).name, "Josiah");
    assertEquals(v.get(1).name, "Lisa");
  }

  @Test
  @SneakyThrows
  void ensureReadingSimpleClassFromInputStreamWorks() {

    @RootElement
    class Value {

      @Attribute
      String name;
    }

    val source = """
        {
          "name": "Josiah"
        }
        """;
    register(Value.class, Value::new);
    val v = condensation.read(Value.class,
        new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));
    assertEquals(v.name, "Josiah");
  }


  @Test
  @SneakyThrows
  void ensureCopyingEnumWorks() {
    enum Test {
      A;
    }

    @RootElement
    class TestType {

      @Attribute
      Test t;
    }

    val condensation = Condensation.create("json");
    ((ReflectiveTypeInstantiator) condensation.getInstantiator())
        .register(TestType.class, TestType::new);
    val t = new TestType();
    t.t = Test.A;
    val u = condensation.read(TestType.class, condensation.getWriter().write(TestType.class, t));
    assertEquals(t.t, Test.A);
  }

  @SneakyThrows
  @Test
  void ensureAliasedEnumPropertyWorks() {
    enum Whatever {
      COOOL;
    }

    @RootElement
    class A {

      @Attribute(alias = @Alias(read = "my-enum", write = "my-enum"))
      Whatever whatever;

      A() {
        whatever = Whatever.COOOL;
      }
    }

    val condensation = Condensation.create("json");
    ((ReflectiveTypeInstantiator) condensation.getInstantiator()).register(A.class, A::new);
    val s = condensation.write(A.class, new A());
    System.out.println(s);
  }

  @Test
  void ensureWritingListDirectlyWorks() throws IOException {
    @NoArgsConstructor
    @AllArgsConstructor
    @RootElement
    class A {

      @Attribute
      String name;
    }
    val condensation = Condensation.create("json");
    val list = List.of(new A("a"), new A("b"));
    val a = condensation.getWriter().writeAll(A.class, list);
    System.out.println(a);
  }

  @Test
  void ensureReadingCollectionWorks() {
    @RootElement
    class A {

      @Attribute
      String name;
    }
    val condensation = Condensation.create("json");
    ((ReflectiveTypeInstantiator) condensation.getInstantiator()).register(A.class, A::new);
    val result = condensation.readAll(A.class, ArrayList::new, "[{\"name\":\"josiah\"}]");
    assertEquals(result.size(), 1);
    assertEquals(result.get(0).name, "josiah");
  }

  @Test
  void ensureBinderWorks() {
    @RootElement
    class A {

      @Attribute
      String name;
    }
    val condensation = Condensation.create("json");
    ((ReflectiveTypeInstantiator) condensation.getInstantiator()).register(A.class, A::new);
    val result = condensation.read(A.class, "{\"name\":\"josiah\"}");
    assertEquals(result.name, "josiah");
  }

  @Test
  void ensureDoubleExampleworks() {

    Condensation condensation = Condensation.create("json");
    double[] values = condensation.read(double[].class, "[1,2,3,4]");
    assertArrayEquals(new double[]{1d, 2d, 3d, 4d}, values);
  }

  @Test
  void ensureIntExampleWorks() {

    Condensation condensation = Condensation.create("json");
    int[] values = condensation.read(int[].class, "[1,2,3,4]");
    assertArrayEquals(new int[]{1, 2, 3, 4}, values);
  }

  @Test
  void ensureMappedPrimitiveExampleWorks() {
    @RootElement
    class KV {

      @Element
      Map<String, Integer> elements;
    }

    val value = "{" + "\"elements\": {" + "\"1\": 1," + "\"2\": 3}" + "} ";

    val condensation = Condensation.create("json");
    ((ReflectiveTypeInstantiator) condensation.getInstantiator()).register(KV.class, KV::new);
    val result = condensation.read(KV.class, value);
    assertEquals(result.elements.size(), 2);
    assertEquals(result.elements.get("2"), 3);
  }

  @Test
  void ensureKeyConverterWorks() {
    class StringToIntegerConverter implements Converter<Integer, String> {

      @Override
      public Integer read(String s) {
        return Integer.valueOf(s);
      }

      @Override
      public String write(Integer integer) {
        return integer == null ? null : integer.toString();
      }
    }
    @RootElement
    class KV {

      @Element
      @Convert(key = StringToIntegerConverter.class)
      Map<Integer, Integer> elements;
    }

    val value = "{" + "\"elements\": {" + "\"1\": 1," + "\"2\": 3}" + "} ";

    val condensation = Condensation.create("json");
    ((ReflectiveTypeInstantiator) condensation.getInstantiator()).register(KV.class, KV::new);
    ((ReflectiveTypeInstantiator) condensation.getInstantiator())
        .register(StringToIntegerConverter.class, StringToIntegerConverter::new);
    val result = condensation.read(KV.class, value);
    assertEquals(result.elements.size(), 2);
    assertEquals(result.elements.get(2), 3);
  }


  private <T> void register(Class<T> type) {
    register(type, () -> Reflect.instantiate(type));
  }

  private <T> void register(Class<T> type, Supplier<T> instantiator) {
    ((ReflectiveTypeInstantiator) condensation.getInstantiator())
        .register(type, instantiator);
  }
}
