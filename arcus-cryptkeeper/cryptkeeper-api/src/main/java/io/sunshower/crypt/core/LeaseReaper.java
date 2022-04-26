package io.sunshower.crypt.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

public interface LeaseReaper {

  ExecutorService getExecutorService();


  BlockingQueue<Lease<?>> getOutstandingLeases();

}
