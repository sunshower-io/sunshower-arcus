package io.sunshower.lang.common.hash.integers;

import io.sunshower.lang.common.hash.HashFunction;

/**
 * Created by haswell on 4/4/16.
 */
public interface IntegerHashFunction extends HashFunction {

    int apply(int value);

    int apply(CharSequence sequence);
}
