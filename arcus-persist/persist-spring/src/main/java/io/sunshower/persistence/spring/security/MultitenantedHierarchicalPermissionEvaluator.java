package io.sunshower.persistence.spring.security;

import java.io.Serializable;
import lombok.val;
import org.springframework.security.acls.AclPermissionEvaluator;
import org.springframework.security.acls.model.AclService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class MultitenantedHierarchicalPermissionEvaluator extends AclPermissionEvaluator {

  public MultitenantedHierarchicalPermissionEvaluator(AclService aclService) {
    super(aclService);
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Object domainObject, Object permission) {
    if (isAdmin(authentication)) {
      return true;
    }
    return super.hasPermission(authentication, domainObject, permission);
  }

  @Override
  public boolean hasPermission(
      Authentication authentication, Serializable targetId, String targetType, Object permission) {
    if (isAdmin(authentication)) {
      return true;
    }
    return super.hasPermission(authentication, targetId, targetType, permission);
  }

  private boolean isAdmin(Authentication authentication) {
    val authorities = authentication.getAuthorities();
    for (GrantedAuthority authority : authorities) {
      if ("administrator".equals(authority.getAuthority())) {
        return true;
      }
    }
    return false;
  }
}
