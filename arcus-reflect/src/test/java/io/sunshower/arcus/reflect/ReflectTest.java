package io.sunshower.arcus.reflect;

import static io.sunshower.arcus.reflect.Reflect.instantiate;
import static io.sunshower.arcus.reflect.Reflect.isCompatible;
import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.lambda.Option;
import io.sunshower.lang.tuple.Pair;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.Test;

public class ReflectTest {

  @Test
  public void ensureReflectConstructorIsInaccessible() throws Exception {
    try {
      Constructor ctor = Reflect.class.getDeclaredConstructor();
      ctor.setAccessible(true);
      ctor.newInstance();
    } catch (InvocationTargetException ex) {
      assertTrue(ex.getTargetException().getMessage().startsWith("No reflect"));
    }
  }

  static class CtorTestClass {
    int snd;
    String fst;

    public CtorTestClass(String fst, int snd) {
      this.fst = fst;
      this.snd = snd;
    }
  }

  @Test
  void ensureIsCompatibleWorksForByte() {
    assertTrue(isCompatible(Byte.class, byte.class));
    assertTrue(isCompatible(byte.class, byte.class));
    assertTrue(isCompatible(byte.class, Byte.class));
    assertTrue(isCompatible(Byte.class, byte.class));
    assertFalse(isCompatible(Byte.class, Object.class));
    assertTrue(isCompatible(Object.class, Byte.class));
  }

  @Test
  void ensureIsCompatibleWorksForCharacter() {
    assertTrue(isCompatible(Character.class, char.class));
    assertTrue(isCompatible(char.class, char.class));
    assertTrue(isCompatible(char.class, Character.class));
    assertTrue(isCompatible(Character.class, char.class));
    assertFalse(isCompatible(Character.class, Object.class));
    assertTrue(isCompatible(Object.class, Character.class));
  }

  @Test
  void ensureIsCompatibleWorksForShort() {
    assertTrue(isCompatible(Short.class, short.class));
    assertTrue(isCompatible(short.class, short.class));
    assertTrue(isCompatible(short.class, Short.class));
    assertTrue(isCompatible(Short.class, short.class));
    assertFalse(isCompatible(Short.class, Object.class));
    assertTrue(isCompatible(Object.class, Short.class));
  }

  @Test
  void ensureIsCompatibleWorksForLong() {
    assertTrue(isCompatible(Long.class, long.class));
    assertTrue(isCompatible(long.class, long.class));
    assertTrue(isCompatible(long.class, Long.class));
    assertTrue(isCompatible(Long.class, long.class));
    assertFalse(isCompatible(Long.class, Object.class));
    assertTrue(isCompatible(Object.class, Long.class));
  }

  @Test
  void ensureIsCompatibleWorksForDouble() {
    assertTrue(isCompatible(Double.class, double.class));
    assertTrue(isCompatible(double.class, double.class));
    assertTrue(isCompatible(double.class, Double.class));
    assertTrue(isCompatible(Double.class, double.class));
    assertFalse(isCompatible(Double.class, Object.class));
    assertTrue(isCompatible(Object.class, Double.class));
  }

  @Test
  void ensureIsCompatibleWorksForInteger() {
    assertTrue(isCompatible(Integer.class, int.class));
    assertTrue(isCompatible(int.class, int.class));
    assertTrue(isCompatible(int.class, Integer.class));
    assertTrue(isCompatible(Integer.class, Integer.class));
    assertFalse(isCompatible(Integer.class, Object.class));
    assertTrue(isCompatible(Object.class, Integer.class));
  }

  @Test
  void ensureIsCompatibleWorksForFloat() {
    assertTrue(isCompatible(Float.class, float.class));
    assertTrue(isCompatible(float.class, float.class));
    assertTrue(isCompatible(float.class, Float.class));
    assertTrue(isCompatible(Float.class, Float.class));
    assertFalse(isCompatible(Float.class, Object.class));
    assertTrue(isCompatible(Object.class, Float.class));
  }

  @Test
  void ensureTypedConstructorIsInvokable() {
    val instance =
        Reflect.instantiate(
            CtorTestClass.class, Pair.of(String.class, "hello"), Pair.of(int.class, 200));
    assertEquals("hello", instance.fst);
    assertEquals(200, instance.snd);
  }

  @Test
  void ensureTypedConstructorIsRetrievable() {
    val o = Reflect.findConstructor(CtorTestClass.class, String.class, int.class);
    assertTrue(o.isSome());
  }

  @Test
  public void ensureStreamCollectsSingleType() {
    class A {}
    List<Class<?>> types = Reflect.linearSupertypes(A.class).collect(Collectors.toList());
    assertEquals(types.contains(A.class), (true));
    assertEquals(types.size(), (2));
    assertEquals(types.contains(Object.class), (true));
    assertEquals(types.contains(A.class), (true));
  }

  @Test
  public void ensureStreamCollectsOnlyTypesAnnotatedWithAnnotation() {
    @Uninherited
    class A {}

    List<Annotation> a =
        Reflect.mapOverHierarchy(A.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
            .collect(Collectors.toList());
    assertEquals(a.size(), (1));
  }

  @Test
  public void ensureStreamCollectsTypesOnFirstLinearSupertype() {
    @Uninherited
    class A {}
    class B extends A {}

    HashSet<Uninherited> collect =
        Reflect.mapOverHierarchy(B.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
            .collect(Collectors.toCollection(HashSet::new));
    assertEquals(collect.size(), (1));
  }

  @Test
  public void ensureStreamCollectsInterfaceOnInterface() {
    class A implements UninheritedIface {}

    HashSet<Uninherited> collect =
        Reflect.mapOverHierarchy(A.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
            .collect(Collectors.toCollection(HashSet::new));
    assertEquals(collect.size(), (1));
  }

  @Test
  public void ensureStreamCollectsannotationOnImplementingLinearSupertype() {
    class A implements UninheritedIface {}

    class B extends A {}

    HashSet<Uninherited> collect =
        Reflect.mapOverHierarchy(B.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
            .collect(Collectors.toCollection(HashSet::new));
    assertEquals(collect.size(), (1));
  }

  @Test
  public void ensureStreamCollectsAnnotationsInCorrectOrder() {

    @Uninherited("a")
    class A implements UninheritedIface, OtherInterface {}

    @Uninherited("b")
    class B extends A implements UninheritedIface {}

    List<String> collect =
        Reflect.mapOverHierarchy(B.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
            .map(Uninherited::value)
            .collect(Collectors.toList());
    assertEquals(collect, (Arrays.asList("b", "", "a", "", "test")));
  }

  @Test
  public void ensureReflectCannotInstantiateNonStaticClass() {
    class A {}
    assertThrows(InstantiationException.class, () -> instantiate(A.class));
  }

  @Test
  public void ensureAttemptingToInstantiateNonInnerClassWithPrivateMethodThrowsException() {
    assertThrows(InstantiationException.class, () -> instantiate(PrivateConstructor.class));
  }

  @Test
  public void ensureConstructorThrowingExceptionPassesCorrectException() {
    try {
      instantiate(ConstructorThrowsException.class);
    } catch (InstantiationException e) {
      assertEquals(e.getCause().getMessage(), ("woah"));
    }
  }

  @Test
  public void ensureInstantiatingInterfaceFails() {
    assertThrows(
        InstantiationException.class,
        () -> {
          instantiate(AbstractClass.class);
        });
  }

  @Uninherited("test")
  interface OtherInterface {}

  @Uninherited
  interface UninheritedIface {}

  abstract static class AbstractClass {
    public AbstractClass() {}
  }

  public static class ConstructorThrowsException {

    public ConstructorThrowsException() {
      throw new IllegalStateException("woah");
    }
  }

  public static class PrivateConstructor {
    private PrivateConstructor() {}
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface Uninherited {
    String value() default "";
  }
}
