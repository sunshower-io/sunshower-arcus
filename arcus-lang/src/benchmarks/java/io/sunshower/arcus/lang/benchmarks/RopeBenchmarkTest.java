package io.sunshower.arcus.lang.benchmarks;

import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

class RopeBenchmarkTest {

  public static void main(String[] args) throws RunnerException {
    var opts = new OptionsBuilder()
        .mode(Mode.Throughput)
        .timeUnit(TimeUnit.SECONDS)
        .warmupTime(TimeValue.seconds(1))
        .warmupIterations(1)
        .measurementIterations(1)
        .threads(2)
        .forks(1)
        .shouldFailOnError(true)
        .shouldDoGC(true).build();
    new Runner(opts).run();
  }



}
