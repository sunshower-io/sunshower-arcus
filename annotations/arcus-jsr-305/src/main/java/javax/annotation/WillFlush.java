package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
/**
 * Used to annotate a method parameter to indicate that this method will flush the resource when
 * done
 */
public @interface WillFlush {}
