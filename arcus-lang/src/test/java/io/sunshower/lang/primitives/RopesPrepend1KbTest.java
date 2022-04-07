package io.sunshower.lang.primitives;

import lombok.val;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;

@State(Scope.Group)
public class RopesPrepend1KbTest extends AbstractRopesPrependBenchmark {


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

  @Benchmark
  @Group("insert")
  public void insertToSmallString() {
    val lhs = string.substring(0, center);
    val rhs = string.substring(center);
    val r = lhs + prependedValue + rhs;
  }

  @Override
  protected byte[] getBytes() {
    return oneKb();
  }
}
