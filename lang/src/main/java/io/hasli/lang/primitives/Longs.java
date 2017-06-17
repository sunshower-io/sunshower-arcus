package io.sunshower.lang.primitives;

/**
 * Created by haswell on 4/29/16.
 */
public class Longs {

    public static byte[] toByteArray(long[] longs) {
        final int length = longs.length;
        final byte[] bytes = new byte[length * 8];
        for(int i = 0, k = 0; i < length; i++) {
            long j = longs[i];
            bytes[k] = (byte) (j >>> 56);
            bytes[k + 1] = (byte) (j >>> 48);
            bytes[k + 2] = (byte) (j >>> 40);
            bytes[k + 3] = (byte) (j >>> 32);
            bytes[k + 4] = (byte) (j >>> 24);
            bytes[k + 5] = (byte) (j >>> 16);
            bytes[k + 6] = (byte) (j >>> 8);
            bytes[k + 7] = (byte) (j);
            k += 8;
        }
        return bytes;
    }



    public static long[] fromByteArray(byte[] longs) {
        final int len = longs.length;
        if(len % 8 != 0) {
            throw new IllegalArgumentException("Byte array must be divisible by 4");
        }
        final long[] result = new long[len / 8];
        for(int i = 0, j = 0; i < result.length; i++) {
            long r = (long) longs[j] << 56L
                    | (long) (longs[j + 1] & 0xFF) << 48
                    | (long) (longs[j + 2] & 0xFF) << 40
                    | (long) (longs[j + 3] & 0xFF) << 32
                    | (long) (longs[j + 4] & 0xFF) << 24
                    | (long) (longs[j + 5] & 0xFF) << 16
                    | (long) (longs[j + 6] & 0xFF) << 8
                    | (long) (longs[j + 7] & 0xFF);
            result[i] = r;
            j += 8;
        }
        return result;
    }















}
