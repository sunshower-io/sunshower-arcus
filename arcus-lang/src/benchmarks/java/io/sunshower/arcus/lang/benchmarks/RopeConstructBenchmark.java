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
public abstract class RopeConstructBenchmark extends AbstractRopeBenchmark {


  private byte[] bytes;


  @Setup(Level.Trial)
  @Group("construct")
  public void setUp() {
    bytes = getBytes();
  }

  protected abstract byte[] getBytes();

  @Benchmark
  @Group("construct")
  public void constructString(Blackhole blackhole) {
    blackhole.consume(new String(bytes));
  }


  @Benchmark
  @Group("construct")
  public void constructRope(Blackhole blackhole) {
    blackhole.consume(new Rope(bytes));
  }


  public static class OneByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1, Bytes.BYTE);
    }
  }

  public static class TenByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10, Bytes.BYTE);
    }
  }



  public static class HundredByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(100, Bytes.BYTE);
    }

  }

  public static class KiloByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1, Bytes.KILOBYTE);
    }
  }

  public static class TenKiloByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10, Bytes.KILOBYTE);
    }
  }


  public static class HundredKiloByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(100, Bytes.KILOBYTE);
    }
  }


  public static class MegabyteByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1, Bytes.MEGABYTE);
    }
  }

  public static class TenMegabyteByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10, Bytes.MEGABYTE);
    }
  }

  public static class HundredMegabyteByteConstructBenchmark extends RopeConstructBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(100, Bytes.MEGABYTE);
    }
  }

}
