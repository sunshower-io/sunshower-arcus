package io.sunshower.persistence.id;

import java.time.Clock;

/** Created by haswell on 7/20/17. */
public interface TimeBased {

  Clock getClock();
}
