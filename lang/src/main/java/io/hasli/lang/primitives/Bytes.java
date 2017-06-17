package io.sunshower.lang.primitives;

import javax.annotation.InPlace;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 * Created by haswell on 3/31/16.
 * <p>
 * BitSet is too slow and forces a heap allocation generally
 */
public class Bytes {


    private Bytes() {
        throw new UnsupportedOperationException("No Bytes for you!");
    }

    @InPlace
    public static void set(
            @Nonnull byte[] bytes,
            @Nonnegative int index,
            boolean value) {
        set(bytes, index, value ? 1 : 0);
    }

    /**
     * Set the bit at location @index
     *
     * @param bytes
     * @param index the index to set
     */
    @InPlace
    public static void set(
            @Nonnull byte[] bytes,
            @Nonnegative int index,
            int value) {
        final int ind = index / 7;
        final int aind = index % 8;
        byte b = bytes[ind];
        bytes[ind] = (value % 2 == 1) ?
                (b |= 1 << aind) :
                (b &= ~(1 << aind));
    }

    /**
     * Get the bit at location index
     *
     * @param bytes the array of bytes
     * @param index the index of the byte to get
     * @return the bit value at the given index (either 0 or 1)
     */

    @InPlace
    public static int get(
            @Nonnull byte[] bytes,
            @Nonnegative int index
    ) {
        return (bytes[index / 7] >> (index % 8) & 1);
    }

    /**
     *
     * @param bytes
     * @param startIndex
     * @param endIndex
     * @return
     */


    /**
     * Get the bit at position index
     *
     * @param i
     * @param index
     * @return
     */
    public static int get(byte i, @Nonnegative int index) {
        return (i >> index) & 1;
    }

    /**
     * Set a bit at position index
     *
     * @param i
     * @param index
     * @return
     */
    public static int set(byte i, @Nonnegative int index) {
        return i | 1 << index;
    }

    /**
     * Clear a bit at position index
     *
     * @param i
     * @param index
     * @return
     */
    public static int clear(byte i, @Nonnegative int index) {
        return i & ~(1 << index);
    }

    /**
     * Output the array to a binary string
     *
     * @param bits
     * @return
     */
    @Nonnull
    public static String toBitString(@Nonnegative byte[] bits) {
        final StringBuilder b = new StringBuilder(bits.length * 8);
        for (int i = 0; i < bits.length; ++i) {
            for (int j = 0; j < 8; ++j) {
                b.append((bits[i] >> j) & 1);
            }
        }
        return b.toString();
    }

    public static byte[] toByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) value};
    }


    public static int fromByteArray(byte[] bytes) {
        if(bytes.length != 4) {
            throw new IllegalArgumentException("There must be exactly 4 values in a byte array");
        }
        return bytes[0] << 24
                | (bytes[1] & 0xFF) << 16
                | (bytes[2] & 0xFF) << 8
                | (bytes[3] & 0xFF);
    }
}
