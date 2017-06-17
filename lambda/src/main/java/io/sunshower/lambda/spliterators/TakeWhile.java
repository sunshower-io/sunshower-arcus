package io.sunshower.lambda.spliterators;

/**
 * Created by haswell on 3/23/16.
 */

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;



public class TakeWhile<T> implements Spliterator<T>{


    private final Spliterator<T> source;
    private final Predicate<T> condition;
    private boolean conditionHolds = true;

    public TakeWhile(Spliterator<T> source, Predicate<T> condition) {
        this.source = source;
        this.condition = condition;
    }


    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return conditionHolds && source.tryAdvance(e -> {
            if (conditionHolds = condition.test(e)) {
                action.accept(e);
            }
        });
    }

    @Override
    public Spliterator<T> trySplit() {
        return null;
    }

    @Override
    public long estimateSize() {
        return conditionHolds ? source.estimateSize() : 0;
    }

    @Override
    public int characteristics() {
        return source.characteristics() &~ Spliterator.SIZED;
    }

    @Override
    public Comparator<? super T> getComparator() {
        return source.getComparator();
    }

}
