package io.sunshower.lang.common.encodings;

import java.io.IOException;
import java.io.OutputStream;

/** Created by haswell on 7/17/17. */
public interface Encoding {

    boolean test(byte[] input);

    boolean test(String input);

    void encode(byte[] input, OutputStream os) throws IOException;

    String encode(byte[] input);

    byte[] decode(String input);

    String encode(String input);
}
