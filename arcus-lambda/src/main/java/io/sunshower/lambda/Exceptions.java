package io.sunshower.lambda;

public class Exceptions {

    @SuppressWarnings("PMD.AvoidCatchingThrowable")
    public static <T, U, V extends Throwable> Option<U> fromExceptional(
            T value, Exceptional<T, U, V> f) {
        try {
            return Option.of(f.apply(value));
        } catch (Throwable ex) {
            return Option.none();
        }
    }
}
