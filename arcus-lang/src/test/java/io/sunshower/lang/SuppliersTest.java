package io.sunshower.lang;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import lombok.val;
import org.junit.jupiter.api.Test;

class SuppliersTest {

  @Test
  void ensureMemoizedSupplierCallsOnlyOnce() {
    val count = new AtomicInteger(0);
    Supplier<Integer> onlyOnce = () -> {
      if(count.get() == 1) {
        throw new IllegalStateException("Error");
      }
      return count.getAndIncrement();
    };

    val memoized = Suppliers.memoize(onlyOnce);
    assertEquals(0, memoized.get());
    assertEquals(0, memoized.get());


  }

}