package com.aire.ux.condensation.mappings;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;

@SuppressFBWarnings
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public final class Mutator extends AccessibleObject {

  @Getter private final Method getter;
  @Getter private final Method setter;

  public Mutator(@NonNull Method getter, @NonNull Method setter) {
    this.getter = getter;
    this.setter = setter;
  }

  @Override
  public void setAccessible(boolean flag) {
    getter.setAccessible(flag);
    setter.setAccessible(flag);
  }

  @Override
  public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
    return Optional.ofNullable(getter.getAnnotation(annotationClass))
        .orElse(setter.getAnnotation(annotationClass));
  }

  @Override
  public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
    return getter.isAnnotationPresent(annotationClass)
        || setter.isAnnotationPresent(annotationClass);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
    val getterAnnotations = getter.getAnnotationsByType(annotationClass);
    val setterAnnotations = setter.getAnnotationsByType(annotationClass);
    val result = new ArrayList<>(List.of(getterAnnotations));
    result.addAll(List.of(setterAnnotations));
    return (T[])
        result.toArray(new Annotation[getterAnnotations.length + setterAnnotations.length]);
  }

  @Override
  public Annotation[] getAnnotations() {
    val getterAnnotations = getter.getAnnotations();
    val setterAnnotations = setter.getAnnotations();
    val result = new ArrayList<Annotation>(List.of(getterAnnotations));
    result.addAll(List.of(setterAnnotations));
    return result.toArray(new Annotation[getterAnnotations.length + setterAnnotations.length]);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
    return Optional.ofNullable(getter.getAnnotation(annotationClass))
        .orElse(setter.getAnnotation(annotationClass));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
    val getterAnnotations = getter.getDeclaredAnnotationsByType(annotationClass);
    val setterAnnotations = setter.getDeclaredAnnotationsByType(annotationClass);
    val result = new ArrayList<>(List.of(getterAnnotations));
    result.addAll(List.of(setterAnnotations));
    return (T[])
        result.toArray(new Annotation[getterAnnotations.length + setterAnnotations.length]);
  }

  @Override
  public Annotation[] getDeclaredAnnotations() {
    val getterAnnotations = getter.getDeclaredAnnotations();
    val setterAnnotations = setter.getDeclaredAnnotations();
    val result = new ArrayList<>(List.of(getterAnnotations));
    result.addAll(List.of(setterAnnotations));
    return result.toArray(new Annotation[getterAnnotations.length + setterAnnotations.length]);
  }

  @SuppressWarnings("unchecked")
  public <T> Class<T> getReturnType() {
    return (Class<T>) getter.getReturnType();
  }

  public Type getGenericType() {
    return getter.getGenericReturnType();
  }
}
