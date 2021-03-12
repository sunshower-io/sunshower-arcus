package io.sunshower.lambda.spliterators;

import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.lambda.Lambda;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.junit.jupiter.api.Test;

class TakeWhileTest {

  @Test
  void ensureTakeWhileWorks() {
    val set = List.of(1, 2, 3, 4, 5, 6);
    val result = Lambda.stream(set).takeWhile(t -> t < 5).collect(Collectors.toList());
    assertEquals(result, List.of(1, 2, 3, 4), "Must produce correct list");
  }
}
