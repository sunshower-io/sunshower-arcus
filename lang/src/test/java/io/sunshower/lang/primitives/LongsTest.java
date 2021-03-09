package io.sunshower.lang.primitives;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.Test;

public class LongsTest {

    @Test
    public void ensureCopyingLongsProducesExpectedResult() {
        long[] l = new long[] {0, -123124, Long.MAX_VALUE, Long.MIN_VALUE};
        long[] k = Longs.fromByteArray(Longs.toByteArray(l));
        assertArrayEquals(l, k);
    }
}
