package io.sunshower.persistence.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Persistence {

  int order() default -1;

  String id() default "";

  String schema() default "";

  String[] scannedPackages() default {};

  Class<?>[] entities() default {};

  String[] migrationLocations() default {};
}
