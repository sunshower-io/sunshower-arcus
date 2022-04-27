package io.sunshower.crypt.core;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("PMD")
final class DefaultLeaseRequest implements LeaseRequest {

  private final Date expiration;
  private final CharSequence password;

  DefaultLeaseRequest(final CharSequence password, final int duration, final TimeUnit unit) {
    this.password = password;
    this.expiration = new Date(unit.toMicros(duration));
  }

  @Override
  public boolean forever() {
    return false;
  }

  @Override
  public Date getExpiration() {
    return expiration;
  }

  @Override
  public CharSequence password() {
    return password;
  }
}
