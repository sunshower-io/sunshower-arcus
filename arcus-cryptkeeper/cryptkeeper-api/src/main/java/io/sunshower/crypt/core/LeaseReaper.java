package io.sunshower.crypt.core;

import java.util.Queue;

public interface LeaseReaper {

  Queue<Lease<?>> getReapedLeases();

  boolean schedule(Lease<?> lease);
}
