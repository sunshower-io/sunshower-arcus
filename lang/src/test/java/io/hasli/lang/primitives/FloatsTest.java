package io.sunshower.lang.primitives;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by haswell on 4/29/16.
 */
public class FloatsTest {


    @Test
    public void ensureCopyingFloatsProducesExpectedResults() {
        float[] a = new float[] {
                1f, 4.0f, 0.0f, 400f,
                1245.1f, 900f, Float.MAX_VALUE, Float.MIN_VALUE,
                Float.NaN
        };
        float[] b = Floats.fromByteArray(Floats.toByteArray(a));
        System.out.println(Arrays.toString(b));
        assertArrayEquals(a, b, 0.0f);
    }





}