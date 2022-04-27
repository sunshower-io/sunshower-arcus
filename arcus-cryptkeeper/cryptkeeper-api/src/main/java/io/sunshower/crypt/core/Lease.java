package io.sunshower.crypt.core;

import java.util.Date;
import lombok.val;

public interface Lease<T> extends AutoCloseable {

  /** @return */
  default boolean isActive() {
    val current = new Date();
    return getExpiration().after(current);
  }

  /** @return whether this lease is expired or not */
  default boolean isExpired() {
    return !isActive();
  }

  /** @return the expiration date of this lease */
  Date getExpiration();

  /** @return the date this lease was aquired */
  Date getLeaseDate();

  /**
   * @return the lease
   * @throws LeaseExpiredException if this lease is not active
   */
  T get() throws LeaseExpiredException;
}
