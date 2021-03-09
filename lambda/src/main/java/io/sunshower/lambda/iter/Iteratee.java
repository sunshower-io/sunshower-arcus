package io.sunshower.lambda.iter;

import java.util.Iterator;

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
