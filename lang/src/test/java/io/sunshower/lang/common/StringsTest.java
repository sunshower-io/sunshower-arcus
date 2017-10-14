package io.sunshower.lang.common;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by haswell on 5/26/16.
 */
public class StringsTest {

    @Test
    public void ensureEmptyStringIsEmpty() {
        assertThat(Strings.isBlank(""), is(true));
    }

}