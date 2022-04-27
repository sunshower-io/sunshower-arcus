package io.sunshower.crypt.core;

import java.util.concurrent.TimeUnit;

public class Leases {

  public static ExpirationBuilder forPassword(CharSequence password) {
    return new ExpirationBuilder(password);
  }

  public static final class ExpirationBuilder {

    final CharSequence password;

    ExpirationBuilder(CharSequence password) {
      this.password = password;
    }

    public LeaseRequest expiresIn(int duration, TimeUnit unit) {
      return new DefaultLeaseRequest(password, duration, unit);
    }
  }
}
