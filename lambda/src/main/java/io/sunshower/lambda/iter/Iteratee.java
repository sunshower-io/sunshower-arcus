package io.sunshower.lambda.iter;

import java.util.Iterator;

/**
 * Created by haswell on 5/25/16.
 */
public interface Iteratee<T, U> {

    enum State {
        Continue,
        Yield,
        Error;
    }

    U yield();

    boolean proceed();

    Iteratee<T, U> next(Iterator<T> iterator);


}
