package io.sunshower.persistence.core;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Revisioned {
  int value();
}
