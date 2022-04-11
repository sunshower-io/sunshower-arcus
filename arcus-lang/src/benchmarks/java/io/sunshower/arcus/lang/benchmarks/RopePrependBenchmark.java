package io.sunshower.arcus.lang.benchmarks;


import io.sunshower.lang.primitives.Rope;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Group)
public class RopePrependBenchmark extends AbstractRopeBenchmark {


  @Setup(Level.Trial)
  @Group("prepend1b")
  public void setUp() {
    var bytes = getBytes();
    rope = new Rope(bytes);
    string = new String(bytes);
  }

  protected byte[] getBytes() {
    return new byte[0];
  }
  @Benchmark
  @Group("prepend")
  public void prependString(Blackhole blackhole) {
    blackhole.consume("hello" + string);
  }

  @Benchmark
  @Group("prepend")
  public void prependRope(Blackhole blackhole) {
    blackhole.consume(rope.prepend("hello"));
  }

  public static class OneByteRopePrependBenchmark extends RopePrependBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1, Bytes.BYTE);
    }
  }

  public static class TenByteRopePrependBenchmark extends RopePrependBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10, Bytes.BYTE);
    }
  }


  public static class OneHundredByteRopePrependBenchmark extends RopePrependBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(100, Bytes.BYTE);
    }
  }


  public static class OneKbRopePrependBenchmark extends RopePrependBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1000, Bytes.BYTE);
    }
  }


  public static class TenKbRopePrependBenchmark extends RopePrependBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1000 * 10, Bytes.BYTE);
    }
  }


  public static class OneHundredKbRopePrependBenchmark extends RopePrependBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1000 * 10 * 10, Bytes.BYTE);
    }
  }


  public static class OneMbRopePrependBenchmark extends RopePrependBenchmark {
    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1000 * 1000, Bytes.BYTE);
    }
  }


  public static class TenMbRopePrependBenchmark extends RopePrependBenchmark {
    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10 * 1000 * 1000, Bytes.BYTE);
    }
  }

  public static class HundredMbRopePrependBenchmark extends RopePrependBenchmark {
    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10 * 10 * 1000 * 1000, Bytes.BYTE);
    }
  }
}
