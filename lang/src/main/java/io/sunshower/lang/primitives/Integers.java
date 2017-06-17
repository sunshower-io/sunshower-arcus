package io.sunshower.lang.primitives;

/**
 * Created by haswell on 4/29/16.
 */
public class Integers {

    private Integers() {}



    public static byte[] toByteArray(int[] integers) {
        final int length = integers.length;
        final byte[] bytes = new byte[length * 4];
        for(int i = 0, k = 0; i < length; i++) {
            int j = integers[i];
            bytes[k] = (byte) (j >>> 24);
            bytes[k + 1] = (byte) (j >>> 16);
            bytes[k + 2] = (byte) (j >>> 8);
            bytes[k + 3] = (byte) j;
            k += 4;
        }
        return bytes;
    }


    public static int[] fromByteArray(byte[] b) {
        final int len = b.length;
        if(len % 4 != 0) {
            throw new IllegalArgumentException("Byte array must be divisible by 4");
        }
        final int[] result = new int[len / 4];
        for(int i = 0, j = 0; i < result.length; i++) {
            int r = b[j] << 24
                    | (b[j + 1] & 0xFF) << 16
                    | (b[j + 2] & 0xFF) << 8
                    | (b[j + 3] & 0xFF);
            result[i] = r;
            j += 4;
        }
        return result;
    }
}
