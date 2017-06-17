package javax.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.security.AccessController;
import java.security.Permission;

/**
 * Created by haswell on 3/24/16.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface Privileged {
    Class<? extends Permission> value();
}
