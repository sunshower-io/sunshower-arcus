package io.sunshower.lambda.spliterators;

import io.sunshower.lambda.Lambda;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TakeWhileTest {

    @Test
    void ensureTakeWhileWorks() {
        val set = Set.of(1, 2, 3, 4, 5, 6);
        val result = Lambda.stream(set).takeWhile(t -> t < 5).collect(Collectors.toList());
        assertEquals(result, List.of(1, 2, 3, 4));
    }
}
