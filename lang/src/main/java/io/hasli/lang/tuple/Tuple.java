package io.sunshower.lang.tuple;

/**
 * Created by haswell on 4/6/16.
 */
public class Tuple {
    private Tuple() {
        throw new RuntimeException("No Tuple instances for you!");
    }

    public static <K, V> Pair<K, V> of(K fst, V snd) {
        return Pair.of(fst, snd);
    }
}
