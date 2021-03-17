package io.sunshower.arcus.config.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.METHOD})
public @interface EnvironmentVariable {

  String key();

  String value();
}
