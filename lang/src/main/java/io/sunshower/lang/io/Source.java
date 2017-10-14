package io.sunshower.lang.io;

import java.io.IOException;

/**
 * Created by haswell on 7/7/17.
 */
public interface Source {
    long size() throws IOException;
    char next() throws IOException;

    boolean hasNext();
}
