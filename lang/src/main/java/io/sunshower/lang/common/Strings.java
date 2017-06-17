package io.sunshower.lang.common;

import javax.annotation.Nullable;

/**
 * Created by haswell on 5/26/16.
 */
public class Strings {
    private Strings() {

    }

    public static boolean isBlank(@Nullable String value) {
        if(value == null) {
            return true;
        }
        return value.trim().isEmpty();
    }
}
