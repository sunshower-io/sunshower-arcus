package io.sunshower.arcus.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

public class MethodFilters {

  /** include only methods that are public */
  public static final Predicate<Method> PUBLIC_METHODS =
      method -> Modifier.isPublic(method.getModifiers());

  /** include only methods that are private */
  public static final Predicate<Method> PRIVATE_METHODS =
      method -> Modifier.isPrivate(method.getModifiers());

  /** include only methods that are protected */
  public static final Predicate<Method> PROTECTED_METHODS =
      method -> Modifier.isProtected(method.getModifiers());

  /**
   * return all the methods with the specified annotation
   *
   * @param type the type of the annotation to filter on
   * @return a stream of methods with the annotation[:w
   */
  public static final Predicate<Method> isAnnotationPresent(Class<? extends Annotation> type) {
    return method -> method.isAnnotationPresent(type);
  }
}
