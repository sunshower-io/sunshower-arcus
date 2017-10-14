package io.sunshower.lambda.iter;

/**
 * Created by haswell on 5/25/16.
 */
public interface Step<T, U, V extends Exception> {

    void read(T input);

    Iteratee.State getState();


    V getError();

    U getResult();

    void reset();

}
