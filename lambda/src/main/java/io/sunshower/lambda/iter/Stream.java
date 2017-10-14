package io.sunshower.lambda.iter;

import java.util.Iterator;

/**
 * Created by haswell on 5/25/16.
 */
public interface Stream<T> extends Iterator<T> {

    T next();

    boolean hasNext();

}
