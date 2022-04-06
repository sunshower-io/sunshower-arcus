package io.sunshower.lang.primitives;

import java.io.IOException;
import org.openjdk.jmh.Main;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Group)
public class RopesCreateSmallStringBenchmark extends AbstractRopesBenchmark {


  private byte[] bytes;

  @Setup
  public void createSmallString() {
    bytes = oneKb();

  }

  @Benchmark
  @Group("oneKb")
  public void oneKbBenchmarkOnRopes() {
    new Rope(bytes);
  }

  @Benchmark
  @Group("oneKb")
  public void oneKbBenchmarkOnStrings() {
    new String(bytes);
  }

  public static void main(String[] args) throws IOException {
    Main.main(args);
  }




}
