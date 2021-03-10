package io.sunshower.lang.common.hash.integers;

import io.sunshower.lang.common.hash.HashFunction;

public interface IntegerHashFunction extends HashFunction {

    int apply(int value);

    int apply(CharSequence sequence);
}
