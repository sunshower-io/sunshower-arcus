package io.sunshower.lang.primitives;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by haswell on 4/29/16.
 */
public class DoublesTest {


    @Test
    public void ensureDoublesArrayIsCopiedCorrectly() {
        double[] d = new double[] {
                0.0d,
                4.0d,
                99.45d,
                113414123451234.4d,
                Double.MAX_VALUE,
                Double.MIN_VALUE
        };

        double[] c = Doubles.fromByteArray(Doubles.toByteArray(d));
        assertArrayEquals(d, c, 0.0d);
    }

}