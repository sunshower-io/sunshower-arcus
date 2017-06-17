package io.sunshower.lambda;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.sunshower.lambda.Lazy.takeWhile;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by haswell on 3/23/16.
 */
public class LazyTest {

    @Test
    public void ensureTakeWhileHoldsForFiniteStream() {
        List<Object> o = takeWhile(Stream.empty(), t -> true)
                .collect(Collectors.toList());
        assertThat(o.isEmpty(), is(true));
    }

    @Test
    public void ensureTakeWhileHoldsForInfiniteStream() {
        List<Integer> is = takeWhile(Stream.iterate(0, i -> i + 1),
                i -> i < 10).collect(Collectors.toList());
        assertThat(is.size(), is(10));
    }

    @Test
    public void ensureComparatorWorks() {
        Optional<Integer> is = takeWhile(Stream.iterate(0, i -> i + 1),
                i -> i < 10).min(Integer::max);
        assertThat(is.get(), is(9));
    }

}