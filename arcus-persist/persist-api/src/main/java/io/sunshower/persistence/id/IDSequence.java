package io.sunshower.persistence.id;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.time.Clock;
import java.util.Objects;

/** Created by haswell on 7/18/17. */
final class IDSequence implements Sequence<Identifier>, NodeAware, TimeBased {

  /** Length of a Flake ID in bytes */
  static final int ID_SIZE = 16;

  /** Length of the node portion of an id */
  static final int NODE_SIZE = 6;

  /** */
  static final int SEQUENCE_MAXIMUM = 0xFFFF;

  /** Every sequence needs its own lock. Making this static would serialize all sequences */
  final Object sequenceLock = new Object();

  /** Sequence lock */
  final Clock clock;

  /** Sequence node ID */
  final InetAddress node;

  /** */
  final InetAddress nodeIdentity;

  final byte[] seed;

  private volatile int sequence;

  private volatile long currentTime;

  private volatile long previousTime;

  private final boolean applyBackpressure;

  public IDSequence(Clock clock, byte[] seed, InetAddress node, boolean applyBackpressure) {
    check(clock, node);
    this.node = node;
    this.clock = clock;
    this.seed = seed;
    this.nodeIdentity = node;
    this.applyBackpressure = applyBackpressure;
  }

  @Override
  public Identifier next() {
    synchronized (sequenceLock) {
      increment();
      ByteBuffer sequenceBytes = ByteBuffer.allocate(ID_SIZE);
      return Identifier.valueOf(
          sequenceBytes.putLong(currentTime).put(seed).putShort((short) sequence).array());
    }
  }

  @SuppressFBWarnings
  private void increment() {
    currentTime = clock.millis();

    if (currentTime != previousTime) {
      sequence = 0;
      previousTime = currentTime;
    } else if (sequence == SEQUENCE_MAXIMUM) {
      if (applyBackpressure) {
        try {
          while (sequence == SEQUENCE_MAXIMUM) {
            Thread.sleep(1);
            if (currentTime != previousTime) {
              sequence = 0;
              previousTime = currentTime;
              return;
            } else {
              synchronized (this) {
                sequence++;
              }
            }
          }
        } catch (InterruptedException ex) {
        }
      }
      throw new IllegalArgumentException(
          "Attempting to generate sequences too quickly.  "
              + "Can't guarantee uniqueness.  "
              + "Try using another sequence instance (with another clock)");
    } else {
      synchronized (this) {
        sequence++;
      }
    }
  }

  @Override
  public Clock getClock() {
    return clock;
  }

  @Override
  public InetAddress getNodeIdentity() {
    return nodeIdentity;
  }

  private void check(Clock clock, InetAddress node) {
    Objects.requireNonNull(node, "Node must not be null!");
    Objects.requireNonNull(clock, "Clock must not be null!");
  }
}
