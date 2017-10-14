package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by haswell on 3/31/16.
 * Indicates that an operation is performed in-place
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface InPlace {

}
