package io.sunshower.lang.common;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class StringsTest {

    @Test
    public void ensureEmptyStringIsEmpty() {
        assertThat(Strings.isBlank(""), is(true));
    }

}