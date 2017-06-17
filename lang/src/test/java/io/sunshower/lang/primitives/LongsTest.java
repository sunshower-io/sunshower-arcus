package io.sunshower.lang.primitives;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by haswell on 4/29/16.
 */
public class LongsTest {


    @Test
    public void ensureCopyingLongsProducesExpectedResult() {
        long[] l = new long[]{0, -123124, Long.MAX_VALUE, Long.MIN_VALUE};
        long[] k = Longs.fromByteArray(Longs.toByteArray(l));
        assertArrayEquals(l, k);

    }

}