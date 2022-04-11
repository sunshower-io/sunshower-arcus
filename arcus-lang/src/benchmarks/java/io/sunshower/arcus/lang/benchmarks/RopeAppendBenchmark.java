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
public class RopeAppendBenchmark extends AbstractRopeBenchmark {

  private StringBuilder str;
  private String toInsert;

  @Setup(Level.Trial)
  @Group("append1b")
  public void setUp() {
    var bytes = getBytes();
    rope = new Rope(bytes);
    string = new String(bytes);
    toInsert = new String(generateCharactersOfLength(100));
  }

  protected byte[] getBytes() {
    return new byte[0];
  }

  @Benchmark
  @Group("append")
  public void appendRope(Blackhole blackhole) {
    blackhole.consume(rope.append(toInsert));
  }

  @Benchmark
  @Group("append")
  public void appendString(Blackhole blackhole) {
    blackhole.consume(string.concat(toInsert));
  }

  public static class OneByteRopeAppendBenchmark extends RopeAppendBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1, Bytes.BYTE);
    }
  }

  public static class TenByteRopeAppendBenchmark extends RopeAppendBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10, Bytes.BYTE);
    }
  }

  public static class OneHundredByteRopeAppendBenchmark extends RopeAppendBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(100, Bytes.BYTE);
    }
  }

  public static class OneKbRopeAppendBenchmark extends RopeAppendBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1000, Bytes.BYTE);
    }
  }

  public static class TenKbRopeAppendBenchmark extends RopeAppendBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1000 * 10, Bytes.BYTE);
    }
  }

  public static class OneHundredKbRopeAppendBenchmark extends RopeAppendBenchmark {

    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1000 * 10 * 10, Bytes.BYTE);
    }
  }

  public static class OneMbRopeAppendBenchmark extends RopeAppendBenchmark {
    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(1000 * 1000, Bytes.BYTE);
    }
  }

  public static class TenMbRopeAppendBenchmark extends RopeAppendBenchmark {
    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10 * 1000 * 1000, Bytes.BYTE);
    }
  }

  public static class HundredMbRopeAppendBenchmark extends RopeAppendBenchmark {
    @Override
    protected byte[] getBytes() {
      return byteString().ofLength(10 * 10 * 1000 * 1000, Bytes.BYTE);
    }
  }
}
