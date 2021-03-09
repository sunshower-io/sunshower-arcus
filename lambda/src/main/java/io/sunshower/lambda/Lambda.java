package io.sunshower.lambda;

import io.sunshower.lambda.spliterators.TakeWhile;
import java.util.Spliterator;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Lambda {

    public static class StreamBuilder<T> {
        final Spliterator<T> spliterator;

        StreamBuilder(final Spliterator<T> stream) {
            this.spliterator = stream;
        }

        /**
         * @param test the predicate to apply
         * @return a stream that continues so long as the predicate returns true
         */
        public Stream<T> takeWhile(Predicate<T> test) {
            return StreamSupport.stream(new TakeWhile<T>(spliterator, test), false);
        }
    }

    /**
     * @param iterable the iterable to take over
     * @param <T> the type of the element produced by the iterator
     * @return a stream-builder that allows additional composition/transformation of the stream
     */
    public static <T> StreamBuilder<T> stream(Iterable<T> iterable) {
        return new StreamBuilder<T>(iterable.spliterator());
    }
}
