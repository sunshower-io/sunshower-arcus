package io.sunshower.lang.common.hash.integers;

public final class StringHash implements IntegerHashFunction {

    public static final IntegerHashFunction INSTANCE = new StringHash();

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
