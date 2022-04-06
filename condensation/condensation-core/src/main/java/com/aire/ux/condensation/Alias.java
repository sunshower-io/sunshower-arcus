package com.aire.ux.condensation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Alias {

  String read() default "..none..";

  String write() default "..none..";
}
