package io.sunshower.arcus.reflect;

import static java.lang.String.format;

import io.sunshower.arcus.incant.MethodDescriptor;
import io.sunshower.lambda.Lazy;
import io.sunshower.lambda.Option;
import io.sunshower.lang.tuple.Pair;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import lombok.val;

/**
 * utility class for Reflective operations
 */
public class Reflect {

  static final String PARAMETERIZED_TYPE_METHOD_NAME = "parameterizedType";

  /**
   * can't create an instance
   */
  private Reflect() {
    throw new RuntimeException("No reflect instances for you!");
  }

  public static <T> Stream<T> collectOverHierarchy(
      @Nonnull Class<?> clazz, @Nonnull Function<Class<?>, Stream<T>> f) {
    return linearSupertypes(clazz)
        .flatMap(i -> Stream.concat(Stream.of(i), Arrays.stream(i.getInterfaces())))
        .flatMap(f::apply);
  }

  public static <T> Stream<T> mapOverHierarchy(
      @Nonnull Class<?> type, @Nonnull Function<Class<?>, Option<T>> f) {
    return collectOverHierarchy(type, cl -> f.apply(cl).stream());
  }

  /**
   * gather all of the linear supertypes of the current class. None of the supertypes will be
   * interfaces (use collectOverHierarchy)
   *
   * @param a the type to gather the supertypes of
   * @return the list of supertypes
   */
  @SuppressWarnings("unchecked")
  public static Stream<Class<?>> linearSupertypes(@Nonnull Class<?> a) {
    return Lazy.takeWhile(Stream.iterate(a, Class::getSuperclass), Objects::nonNull);
  }

  public static <R> Option<Constructor<R>> findConstructor(
      @Nonnull Class<R> type, @Nonnull Class<?>... constructorArguments) {
    try {
      return Option.of(type.getConstructor(constructorArguments));
    } catch (Exception e) {
      return Option.none();
    }
  }

  @SuppressWarnings("unchecked")
  public static <T, U> Optional<U> fieldValue(@Nonnull T instance, String fieldName) {
    return fieldValue((Class<T>) instance.getClass(), instance, fieldName);
  }

  @SuppressWarnings("unchecked")
  public static <T, U> Optional<U> fieldValue(Class<T> type, T instance, String fieldName) {
    return collectOverHierarchy(type, type1 -> {
      try {
        val field = type1.getDeclaredField(fieldName);
        field.trySetAccessible();
        return Stream.of(field);
      } catch (NoSuchFieldException ex) {
        return Stream.empty();
      }
    }).findAny().flatMap(t -> {
      try {
        return Optional.of((U) t.get(instance));
      } catch (IllegalAccessException e) {
        return Optional.empty();
      }
    });
  }

  public static <R> R instantiate(
      @Nonnull Class<R> type, @Nonnull Pair<Class<?>, Object>... constructorArguments) {
    val ctorTypes = Stream.of(constructorArguments).map(t -> t.fst).toArray(Class<?>[]::new);
    val ctorArgs = Stream.of(constructorArguments).map(t -> t.snd).toArray();
    validate(ctorTypes, ctorArgs);
    try {
      val ctor = type.getDeclaredConstructor(ctorTypes);
      return ctor.newInstance(ctorArgs);
    } catch (NoSuchMethodException e) {
      throw new InstantiationException("Class must declare a public, no-arg constructor");
    } catch (IllegalAccessException e) {
      throw new InstantiationException("Constructor must be public");
    } catch (InvocationTargetException e) {
      throw new InstantiationException("Constructor threw exception", e.getTargetException());
    } catch (java.lang.InstantiationException e) {
      throw new InstantiationException(
          "Failed to instantiate class.  " + "Did you pass an interface or abstract class?", e);
    }
  }

  private static void validate(Class<?>[] types, Object[] args) {
    for (int i = 0; i < types.length; i++) {
      val nextType = types[i];
      val nextInstance = args[i];
      if (nextInstance != null) {
        if (!isCompatible(nextType, nextInstance.getClass())) {
          throw new InstantiationException(
              format(
                  "Error: type-mismatch.  Instance of type '%s' is not assignable to type '%s'",
                  nextInstance.getClass(), nextInstance.getClass()));
        }
      }
    }
  }

  /**
   * determine if classes are compatible accounting for boxing
   *
   * @param nextType the comparative class
   * @param t        the type to check for compatibility with nextType
   * @return true if they're compatible, false otherwise
   */
  public static boolean isCompatible(Class<?> nextType, Class<?> t) {

    if (nextType.isAssignableFrom(t)) {
      return true;
    }
    if (nextType.isPrimitive() || t.isPrimitive()) {
      if ((nextType == Float.class || nextType == float.class)
          && (t == float.class || t == Float.class)) {
        return true;
      }

      if ((nextType == Integer.class || nextType == int.class)
          && (t == Integer.class || t == int.class)) {
        return true;
      }

      if ((nextType == Double.class || nextType == double.class)
          && (t == Double.class || t == double.class)) {
        return true;
      }

      if ((nextType == Long.class || nextType == long.class)
          && (t == Long.class || t == long.class)) {
        return true;
      }

      if ((nextType == Short.class || nextType == short.class)
          && (t == Short.class || t == short.class)) {
        return true;
      }

      if ((nextType == Character.class || nextType == char.class)
          && (t == Character.class || t == char.class)) {
        return true;
      }

      if ((nextType == Byte.class || nextType == byte.class)
          && (t == Byte.class || t == byte.class)) {
        return true;
      }

      if ((nextType == Boolean.class || nextType == boolean.class)
          && (t == Boolean.class || t == boolean.class)) {
        return true;
      }
    }
    return false;
  }

  /**
   * instantiate a class using its default, no-arg constructor
   *
   * @param aClass the class to instantiate
   * @param <R>    the generic type of the class
   * @return a new instance
   * @throws InstantiationException wrapping any thrown reflectiveoperation exception
   */
  @SuppressWarnings("unchecked")
  public static <R> R instantiate(@Nonnull Class<R> aClass) {
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
          "Failed to instantiate class.  " + "Did you pass an interface or abstract class?", e);
    }
  }

  /**
   * @param type          the type to check for a matching method
   * @param methodName    the method name to check for
   * @param argumentTypes the parameter types to check
   * @return true if a matching method is found
   */
  public static boolean hasMethod(
      @Nonnull Class<?> type, @Nonnull String methodName, Class<?>... argumentTypes) {
    val declaredMethods = type.getDeclaredMethods();
    for (val method : declaredMethods) {
      if (methodName.equals(method.getName())) {
        return Arrays.equals(argumentTypes, method.getParameterTypes());
      }
    }
    return false;
  }

  /**
   * @param parameter the parameter to get the parameterized types of
   * @return the type-parameters, if any
   */
  public static Option<Type[]> getTypeParametersOfParameter(Parameter parameter) {
    val types = parameter.getParameterizedType();
    if (types instanceof ParameterizedType) {
      val t = (ParameterizedType) types;
      return Option.some(t.getActualTypeArguments());
    }
    return Option.none();
  }

  /**
   * return a method matching the provided criteria
   *
   * @param host          the host-class to search
   * @param methodName    the method-name
   * @param argumentTypes the argument-types
   * @return an option containing the matching method, or none if it does not exist
   */
  public static Option<Method> getMethod(
      Class<?> host, String methodName, Class<?>... argumentTypes) {
    val declaredMethods = host.getDeclaredMethods();
    for (val method : declaredMethods) {
      if (methodName.equals(method.getName())) {
        if (Arrays.equals(argumentTypes, method.getParameterTypes())) {
          return Option.some(method);
        }
      }
    }
    return Option.none();
  }

  /**
   * construct a stream over the specified hierarchy with the specified hierarchy traversal mode
   *
   * @param type the base type
   * @param mode the traversal mode
   * @return a stream of types adhering to the traversal mode
   */
  public static Stream<Class<?>> hierarchyOf(@Nonnull Class<?> type, HierarchyTraversalMode mode) {
    return mode.apply(type);
  }

  /**
   * Filter all of the methods from all of the types over the specified hierarchytraversalmode
   *
   * @param type   the base of the hierarchy
   * @param mode   the traversal mode
   * @param filter the method filter to apply
   * @return a list of methods matching the filter
   */
  public static Stream<Method> methodsMatching(
      @Nonnull Class<?> type, HierarchyTraversalMode mode, Predicate<Method> filter) {
    return hierarchyOf(type, mode)
        .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
        .filter(filter);
  }

  /**
   * @param type          the type to search for a matching method
   * @param methodName    the name of the method
   * @param argumentTypes the argument types of the method
   * @param <T>           the generic type of the class
   * @param <U>           the result-type of the method
   * @return an option containing a matching method-descriptor or none
   */
  public static <T, U> Option<MethodDescriptor<T, U>> getMethodDescriptor(
      Class<T> type, String methodName, Class<?>... argumentTypes) {
    val declaredMethods = type.getDeclaredMethods();
    for (val method : declaredMethods) {
      if (methodName.equals(method.getName())) {
        if (Arrays.equals(argumentTypes, method.getParameterTypes())) {
          return Option.some(new MethodDescriptor<T, U>(type, method));
        }
      }
    }
    return Option.none();
  }
}
