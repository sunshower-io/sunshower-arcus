package io.sunshower.persistence.spring.security;

import org.springframework.security.acls.domain.AclAuthorizationStrategy;
import org.springframework.security.acls.domain.AclAuthorizationStrategyImpl;
import org.springframework.security.core.GrantedAuthority;

public class MultitenantedAclAuthorizationStrategy extends AclAuthorizationStrategyImpl implements
    AclAuthorizationStrategy {
  public MultitenantedAclAuthorizationStrategy(GrantedAuthority root) {
    super(root);
  }

}