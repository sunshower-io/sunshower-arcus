package io.sunshower.lang.tuple;

import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by haswell on 4/6/16.
 */
public final class Pair<K, V> {

    public final K fst;
    public final V snd;

    public Pair(K fst, V snd) {
        this.fst = fst;
        this.snd = snd;
    }


    public K fst() {
        return fst;
    }

    public V snd() {
        return snd;
    }

    public static <K, V> Pair<K, V> of(K k, V v) {
        return new Pair<>(k, v);
    }

    public static <K, V> Stream<Pair<K, V>> bindFst(K k, V v) {
        return k == null ?
                Stream.empty() : Stream.of(of(k, v));
    }

    public <T, U>  Pair<T, U> map(Function<Pair<K, V>, Pair<T, U>> f) {
        return f.apply(this);
    }

    @Override
    public String toString() {
        return "(" + fst + "," + snd + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pair<?, ?> pair = (Pair<?, ?>) o;

        if (fst != null ? !fst.equals(pair.fst) : pair.fst != null) return false;
        return snd != null ? snd.equals(pair.snd) : pair.snd == null;

    }

    @Override
    public int hashCode() {
        int result = fst != null ? fst.hashCode() : 0;
        result = 31 * result + (snd != null ? snd.hashCode() : 0);
        return result;
    }
}
