package io.sunshower.lang;

/**
 * Created by haswell on 4/6/16.
 */
public abstract class Either<K, V> {


    public abstract boolean isLeft();

    public abstract boolean isRight();


}
