package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StringsTest {

    @Test
    void ensureEmptyStringIsEmpty() {
        assertEquals(Strings.isBlank(""), true);
    }
}
