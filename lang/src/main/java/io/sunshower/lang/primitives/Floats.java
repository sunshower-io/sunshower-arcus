package io.sunshower.lang.primitives;

import java.nio.ByteBuffer;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.floatToRawIntBits;
import static java.lang.Float.intBitsToFloat;

/**
 * Created by haswell on 4/29/16.
 */
public class Floats {

    public static final byte[] toByteArray(float[] floats) {
        int length = floats.length;
        byte[] result = new byte[length * 4];
        for (int i = 0, j = 0; i < length; i++) {
            int data = floatToIntBits(floats[i]);
            result[j] = (byte) (data >>> 24);
            result[j + 1] = (byte) (data >>> 16);
            result[j + 2] = (byte) (data >>> 8);
            result[j + 3] = (byte) (data);
            j += 4;
        }
        return result;
    }

    public static final float[] fromByteArray(byte[] floats) {
        final int len = floats.length;
        if(len % 4 != 0) {
            throw new IllegalArgumentException("Byte array must be divisible by 4");
        }
        final float[] result = new float[len / 4];
        for(int i = 0, j = 0; i < result.length; i++) {
            int r = floats[j] << 24
                    | (floats[j + 1] & 0xFF) << 16
                    | (floats[j + 2] & 0xFF) << 8
                    | (floats[j + 3] & 0xFF);
            result[i] = intBitsToFloat(r);
            j += 4;
        }
        return result;
    }
}
