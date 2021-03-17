package io.sunshower.arcus.config.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
public @interface WithEnvironment {

  EnvironmentVariable[] value() default {};

  EnvironmentVariable[] variables() default {};
}
