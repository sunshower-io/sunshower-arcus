package io.sunshower.lang.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class StringsTest {

    @Test
    public void ensureEmptyStringIsEmpty() {
        assertThat(Strings.isBlank(""), is(true));
    }
}
