package com.aire.ux.condensation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Element {

  /** @return an alias configuration */
  Alias alias() default @Alias();

  /** @return an override configuration */
  Mutator mutateVia() default @Mutator();

  @interface Mutator {

    /** @return the method name to read the property value through */
    String readVia() default "..none..";

    /** @return the method name to write the property value through */
    String writeVia() default "..none..";
  }
}
