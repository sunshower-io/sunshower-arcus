package io.sunshower.lang.common.hash.integers;

/**
 * Created by haswell on 4/4/16.
 */
public final class StringHash implements IntegerHashFunction {

    public static IntegerHashFunction INSTANCE = new StringHash();

    @Override
    public int apply(int value) {
        return 31 * value;
    }

    @Override
    public int apply(CharSequence sequence) {
        return sequence.hashCode();
    }

    @Override
    public int bits() {
        return 32;
    }
}
