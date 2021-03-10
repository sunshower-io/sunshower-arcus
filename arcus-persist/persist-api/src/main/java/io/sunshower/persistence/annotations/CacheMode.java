package io.sunshower.persistence.annotations;

import java.lang.annotation.*;

@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CacheMode {

  Mode value() default Mode.Local;

  enum Mode {
    Local,
    None,
    Grid,
    Container
  }
}
