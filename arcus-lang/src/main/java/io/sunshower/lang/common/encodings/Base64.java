package io.sunshower.lang.common.encodings;

import java.io.IOException;
import java.io.OutputStream;

public class Base64 implements Encoding {

    @Override
    public boolean test(byte[] input) {
        return false;
    }

    @Override
    public boolean test(String input) {
        return false;
    }

    @Override
    public void encode(byte[] input, OutputStream os) throws IOException {}

    @Override
    public String encode(byte[] input) {
        return null;
    }

    @Override
    public byte[] decode(String input) {
        return new byte[0];
    }

    @Override
    public String encode(String input) {
        return null;
    }
}
