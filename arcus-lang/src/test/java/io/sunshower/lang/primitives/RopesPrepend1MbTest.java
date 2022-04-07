package io.sunshower.lang.primitives;

import lombok.val;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;

public class RopesPrepend1MbTest extends AbstractRopesPrependBenchmark {


  @Benchmark
  @Group("prepend")
  public void prependToSmallRope() {
    val r = rope.prepend(prependedValue);
  }

  @Benchmark
  @Group("prepend")
  public void prependToSmallString() {
    val r = prependedValue + string;
  }

  @Benchmark
  @Group("insert")
  public void insertToSmallRope() {
    val r = rope.insert(center, prependedValue);
  }

  @Override
  protected byte[] getBytes() {
    return oneMb();
  }
}
