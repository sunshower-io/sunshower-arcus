package io.sunshower.arcus.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** this class is intended to be loaded by an IOC technology like Spring. */
@Documented
@Target(ElementType.TYPE)
@Repeatable(Configurations.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configure {

  /**
   * the configuration class to bind
   *
   * @return the class to bind the configuration to
   */
  Class<?> value();

  /**
   * @return the name of this configuration. Names may not be duplicated anywhere within this
   *     environment
   */
  String name() default "__default__";

  /**
   * override defaults
   *
   * @return the location overrides
   */
  Location from() default @Location();
}
