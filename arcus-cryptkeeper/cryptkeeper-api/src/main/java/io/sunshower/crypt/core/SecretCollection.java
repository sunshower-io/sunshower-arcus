package io.sunshower.crypt.core;

import java.util.Collection;

public interface SecretCollection
    extends Collection<Secret>, AutoCloseable, Lease<SecretCollection> {

  boolean closed();

  void close();
}
