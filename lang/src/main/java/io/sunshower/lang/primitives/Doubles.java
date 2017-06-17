package io.sunshower.lang.primitives;

import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.longBitsToDouble;

/**
 * Created by haswell on 4/29/16.
 */
public class Doubles {

    public static final byte[] toByteArray(double[] ds) {
        int length = ds.length;
        byte[] result = new byte[length * 8];
        for (int i = 0, j = 0; i < length; i++) {
            long data = doubleToLongBits(ds[i]);
            result[j] = (byte) (data >>> 56);
            result[j + 1] = (byte) (data >>> 48);
            result[j + 2] = (byte) (data >>> 40);
            result[j + 3] = (byte) (data >>> 32);
            result[j + 4] = (byte) (data >>> 24);
            result[j + 5] = (byte) (data >>> 16);
            result[j + 6] = (byte) (data >>> 8);
            result[j + 7] = (byte) (data);
            j += 8;
        }
        return result;
    }

    public static final double[] fromByteArray(byte[] floats) {
        final int len = floats.length;
        if(len % 8 != 0) {
            throw new IllegalArgumentException("Byte array must be divisible by 8");
        }
        final double[] result = new double[len / 8];
        for(int i = 0, j = 0; i < result.length; i++) {
            long r = (long) floats[j] << 56
                    | (long) (floats[j + 1] & 0xFF) << 48
                    | (long) (floats[j + 2] & 0xFF) << 40
                    | (long) (floats[j + 3] & 0xFF) << 32
                    | (long) (floats[j + 4] & 0xFF) << 24
                    | (long) (floats[j + 5] & 0xFF) << 16
                    | (long) (floats[j + 6] & 0xFF) << 8
                    | (long) (floats[j + 7] & 0xFF);
            result[i] = longBitsToDouble(r);
            j += 8;
        }
        return result;
    }
}
