package io.sunshower.arcus.lang.benchmarks;

import io.sunshower.lang.primitives.Rope;
import lombok.val;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Group;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Group)
public abstract class RopeInsertBenchmark extends AbstractRopeBenchmark {

  protected int index;
  private String substring;
  private byte[] bytes;

  @Setup(Level.Trial)
  @Group("insert")
  public void setUp() {
    bytes = getBytes();
    rope = new Rope(bytes);
    string = new String(bytes);
    index = getIndex();
    substring = new String(byteString().ofLength(100, Bytes.BYTE));
  }

  protected byte[] getBytes() {
    return new byte[0];
  }
  
  protected int getIndex() {
    return bytes.length / 2;
  }
  
  @Benchmark
  @Group("insert")
  public void prependString(Blackhole blackhole) {
    val lhs = string.substring(0, index);
    val rhs = string.substring(index);
    blackhole.consume(lhs + substring + rhs);
  }

  @Benchmark
  @Group("insert")
  public void prependRope(Blackhole blackhole) {
    blackhole.consume(rope.insert(index, substring));
  }

  public static class OneByteInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1, Bytes.BYTE);
    }
  }

  public static class TenByteRopeInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10, Bytes.BYTE);
    }

  }


  public static class HundredbyteRopeInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(100, Bytes.BYTE);
    }
  }


  public static class KilobyteRopeInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1, Bytes.KILOBYTE);
    }
  }


  public static class TenKbRopeInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10, Bytes.KILOBYTE);
    }
  }


  public static class HundredKilobyteInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(100, Bytes.KILOBYTE);
    }
  }


  public static class MegabyteInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1, Bytes.MEGABYTE);
    }
  }


  public static class TenMegabyteInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10, Bytes.MEGABYTE);
    }
  }


  public static class HundredMbRopeInsertBenchmark extends RopeInsertBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(100, Bytes.MEGABYTE);
    }
  }
}
