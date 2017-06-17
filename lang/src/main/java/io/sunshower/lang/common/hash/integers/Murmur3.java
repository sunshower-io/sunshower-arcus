package io.sunshower.lang.common.hash.integers;

/**
 * Created by haswell on 4/4/16.
 */
public final class Murmur3 implements IntegerHashFunction {


    private static final int Constant1 = 0xcc9e2d51;
    private static final int Constant2 = 0x1b873593;

    private final int seed;

    public Murmur3(int seed) {
        this.seed = seed;
    }

    @Override
    public int bits() {
        return 32;
    }


    @Override
    public String toString() {
        return "Murmur3(blocksize: 32 bits)(" + seed + ")";
    }


    @Override
    public int hashCode() {
        return getClass().hashCode() ^ seed;
    }

    private static int createK(int k) {
        k *= Constant1;
        k = (k << 15) | (k >>> 15);
        k *= Constant2;
        return k;
    }

    private static int createH(int h, int k) {
        h ^= k;
        h = (h << 13) | (h >>> 13);
        h = h * 5 + 0xe6546b64;
        return h;
    }

    private static int mix(int h1, int length) {
        h1 ^= length;
        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;
        return h1;
    }

    @Override
    public int apply(int value) {
        int k1 = createK(value);
        int h1 = createH(seed, k1);
        return mix(h1, 4);
    }

    @Override
    public int apply(CharSequence sequence) {
        int h1 = seed;
        for (int i = 1; i < sequence.length(); i += 2) {
            int k1 = sequence.charAt(i - 1) | (sequence.charAt(i) << 16);
            k1 = createK(k1);
            h1 = createH(h1, k1);
        }

        if ((sequence.length() & 1) == 1) {
            int k1 = sequence.charAt(sequence.length() - 1);
            k1 = createK(k1);
            h1 ^= k1;
        }
        return mix(h1, 2 * sequence.length());
    }

}
