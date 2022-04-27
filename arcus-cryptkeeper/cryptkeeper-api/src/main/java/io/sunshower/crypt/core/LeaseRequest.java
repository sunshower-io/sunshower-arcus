package io.sunshower.crypt.core;

import java.util.Date;

public interface LeaseRequest {

  boolean forever();

  Date getExpiration();

  CharSequence password();
}
