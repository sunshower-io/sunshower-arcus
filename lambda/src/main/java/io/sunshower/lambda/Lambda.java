package io.sunshower.lambda;

import io.sunshower.lambda.spliterators.TakeWhile;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Lambda {

    public static class StreamBuilder<T> {
        final Stream<T> stream;

        StreamBuilder(final Stream<T> stream) {
            this.stream = stream;
        }

        public Stream<T> takeWhile(Predicate<T> test) {
            return StreamSupport.stream(new TakeWhile<T>(stream.spliterator(), test), false);
        }
    }

    public static <T> StreamBuilder<T> stream(Iterable<T> iterable) {
        return new StreamBuilder<T>(StreamSupport.stream(iterable.spliterator(), false));
    }
}
