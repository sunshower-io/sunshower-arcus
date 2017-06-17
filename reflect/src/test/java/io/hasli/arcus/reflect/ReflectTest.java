package io.sunshower.arcus.reflect;

import io.sunshower.lambda.Option;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static io.sunshower.arcus.reflect.Reflect.instantiate;


/**
 * Created by haswell on 3/23/16.
 */
public class ReflectTest {


    @Test
    public void ensureReflectConstructorIsInaccessible() throws Exception {
        try {
            Constructor ctor = Reflect.class.getDeclaredConstructor();
            ctor.setAccessible(true);
            ctor.newInstance();
        } catch(InvocationTargetException ex) {
            Assert.assertThat(ex.getTargetException().getMessage().startsWith("No reflect"), CoreMatchers.is(true));
        }
    }

    @Test
    public void ensureStreamCollectsSingleType() {
        class A{}
        List<Class<?>> types = Reflect
                .linearSupertypes(A.class).collect(Collectors.toList());
        Assert.assertThat(types.contains(A.class), CoreMatchers.is(true));
        Assert.assertThat(types.size(), CoreMatchers.is(2));
        Assert.assertThat(types.contains(Object.class), CoreMatchers.is(true));
        Assert.assertThat(types.contains(A.class), CoreMatchers.is(true));
    }


    @Test
    public void ensureStreamCollectsOnlyTypesAnnotatedWithAnnotation() {
        @Uninherited
        class A {

        }

        List<Annotation> a = Reflect.mapOverHierarchy(A.class,
                i -> Option.of(i.getAnnotation(Uninherited.class)))
                .collect(Collectors.toList());
        Assert.assertThat(a.size(), CoreMatchers.is(1));
    }

    @Test
    public void ensureStreamCollectsTypesOnFirstLinearSupertype() {
        @Uninherited
        class A { }
        class B extends A {}

        HashSet<Uninherited> collect = Reflect.mapOverHierarchy(
                B.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
                .collect(Collectors.toCollection(HashSet::new));
        Assert.assertThat(collect.size(), CoreMatchers.is(1));
    }


    @Test
    public void ensureStreamCollectsInterfaceOnInterface() {
        class A implements UninheritedIface {}


        HashSet<Uninherited> collect = Reflect.mapOverHierarchy(
                A.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
                .collect(Collectors.toCollection(HashSet::new));
        Assert.assertThat(collect.size(), CoreMatchers.is(1));
    }

    @Test
    public void ensureStreamCollectsannotationOnImplementingLinearSupertype() {
        class A implements UninheritedIface{}

        class B extends A{}


        HashSet<Uninherited> collect = Reflect.mapOverHierarchy(
                B.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
                .collect(Collectors.toCollection(HashSet::new));
        Assert.assertThat(collect.size(), CoreMatchers.is(1));

    }

    @Test
    public void ensureStreamCollectsAnnotationsInCorrectOrder() {

        @Uninherited("a")
        class A implements UninheritedIface, OtherInterface {}

        @Uninherited("b")
        class B extends A implements UninheritedIface{}

        List<String> collect = Reflect.mapOverHierarchy(
                B.class, i -> Option.of(i.getAnnotation(Uninherited.class)))
                .map(Uninherited::value).collect(Collectors.toList());
        Assert.assertThat(collect, CoreMatchers.is(Arrays.asList("b", "", "a", "", "test")));
    }

    @Test(expected = InstantiationException.class)
    public void ensureReflectCannotInstantiateNonStaticClass() {
        class A {}
        instantiate(A.class);
    }

    @Test(expected = InstantiationException.class)
    public void ensureAttemptingToInstantiateNonInnerClassWithPrivateMethodThrowsException() {
        instantiate(PrivateConstructor.class);
    }

    @Test
    public void ensureConstructorThrowingExceptionPassesCorrectException() {
        try {
            instantiate(ConstructorThrowsException.class);
        } catch(InstantiationException e)  {
            Assert.assertThat(e.getCause().getMessage(), CoreMatchers.is("woah"));
        }
    }

    @Test(expected = InstantiationException.class)
    public void ensureInstantiatingInterfaceFails() {
        instantiate(AbstractClass.class);
    }


    @Uninherited("test")
    interface OtherInterface {}


    @Uninherited
    interface UninheritedIface {

    }

    static abstract class AbstractClass {
        public AbstractClass() {}
    }

    public static class ConstructorThrowsException {

        public ConstructorThrowsException() {
            throw new IllegalStateException("woah");
        }

    }

    public static class PrivateConstructor {
        private PrivateConstructor(){}
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface Uninherited {
        String value() default "";
    }

}