package io.sunshower.persistence.auth;

import io.sunshower.common.Identifier;

public interface Principal extends java.security.Principal {

  Identifier getId();

  int getAgentType();
}
