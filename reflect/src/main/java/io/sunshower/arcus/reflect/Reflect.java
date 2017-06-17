package io.sunshower.arcus.reflect;

import io.sunshower.lambda.Lazy;
import io.sunshower.lambda.Option;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.sunshower.lambda.Lazy.takeWhile;

/**
 * Created by haswell on 3/23/16.
 */
public class Reflect {

    private Reflect(){
        throw new RuntimeException("No reflect instances for you!");
    }

    public static <T> Stream<T> collectOverHierarchy(
            Class<?> clazz,
            Function<Class<?>, Stream<T>> f
    ) {
        return linearSupertypes(clazz)
                .flatMap(i -> Stream.concat(Stream.of(i),
                        Arrays.stream(i.getInterfaces())))
                .flatMap(i -> f.apply(i));
    }

    public static <T> Stream<T> mapOverHierarchy(Class<?> type, Function<Class<?>, Option<T>> f) {
        return collectOverHierarchy(type, cl -> f.apply(cl).stream());
    }




    @SuppressWarnings("unchecked")
    public static Stream<Class<?>> linearSupertypes(Class<?> a) {
        return Lazy.takeWhile(Stream.iterate(a, Class::getSuperclass), i -> i != null);
    }

    @SuppressWarnings("unchecked")
    public static <R> R instantiate(Class<R> aClass) {
        try {
            final Constructor<R> ctor = aClass.getDeclaredConstructor();
            return ctor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new InstantiationException("Class must declare a public, no-arg constructor");
        } catch (IllegalAccessException e) {
            throw new InstantiationException("Constructor must be public");
        } catch (InvocationTargetException e) {
            throw new InstantiationException("Constructor threw exception", e.getTargetException());
        } catch (java.lang.InstantiationException e) {
            throw new InstantiationException(
                    "Failed to instantiate class.  " +
                            "Did you pass an interface or abstract class?",  e);
        }
    }
}
