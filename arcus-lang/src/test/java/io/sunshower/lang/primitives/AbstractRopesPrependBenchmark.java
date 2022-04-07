package io.sunshower.lang.primitives;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@State(Scope.Benchmark)
public abstract class AbstractRopesPrependBenchmark extends AbstractRopesBenchmark {


  protected byte[] bytes;
  protected Rope rope;
  protected String string;
  protected CharSequence prependedValue;
  protected int center;

  @Setup
  public void setUp() {
    bytes = getBytes();
    rope = new Rope(bytes);
    string = new String(bytes);
    prependedValue = new String(generateCharactersOfLength(256));

    center = string.length();
  }

  protected abstract byte[] getBytes();

}
