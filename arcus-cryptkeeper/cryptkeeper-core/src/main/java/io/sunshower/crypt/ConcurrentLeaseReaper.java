package io.sunshower.crypt;

import io.sunshower.crypt.core.Lease;
import io.sunshower.crypt.core.LeaseReaper;
import io.sunshower.crypt.core.SecretService;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import lombok.extern.java.Log;

@Log
public class ConcurrentLeaseReaper implements LeaseReaper {

  private final Timer timer;
  private final Queue<Lease<?>> leases;
  private final SecretService service;

  public ConcurrentLeaseReaper(SecretService service) {
    this(true, service);
  }

  public ConcurrentLeaseReaper(boolean daemon, final SecretService service) {
    this.service = service;
    this.leases = new LinkedBlockingDeque<>();
    this.timer = new Timer("concurrent-lease-reaper-1", daemon);
  }

  @Override
  public Queue<Lease<?>> getReapedLeases() {
    return leases;
  }

  @Override
  public boolean schedule(Lease<?> lease) {
    log.log(
        Level.INFO,
        "Scheduling new lease ''{0}'': Start: ''{1}'', end: ''{2}''...",
        new Object[] {lease, lease.getLeaseDate(), lease.getExpiration()});
    timer.schedule(new LeaseTimerTask(lease), lease.getExpiration());
    return true;
  }

  final class LeaseTimerTask extends TimerTask {

    final Lease<?> lease;

    public LeaseTimerTask(Lease<?> lease) {
      this.lease = lease;
    }

    @Override
    public void run() {
      log.log(Level.INFO, "Releasing lease: ''{0}''", lease);
      service.close(lease);
      leases.offer(lease);
    }
  }
}
