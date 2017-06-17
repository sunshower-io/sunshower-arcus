package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by haswell on 3/31/16.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Between {
    int end();
    int begin();
}
