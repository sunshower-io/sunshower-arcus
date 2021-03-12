package io.sunshower.arcus.reflect;

import static io.sunshower.arcus.reflect.Reflect.instantiate;
import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.lambda.Option;
import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
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
