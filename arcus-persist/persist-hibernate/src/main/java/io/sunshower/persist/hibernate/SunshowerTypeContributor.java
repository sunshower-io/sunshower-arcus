package io.sunshower.persist.hibernate;

import io.sunshower.persist.hibernate.types.FlakeIdentifierType;
import org.hibernate.boot.model.TypeContributions;
import org.hibernate.boot.model.TypeContributor;
import org.hibernate.service.ServiceRegistry;

public class SunshowerTypeContributor implements TypeContributor {
  @Override
  public void contribute(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
    typeContributions.contributeType(FlakeIdentifierType.INSTANCE);
  }
}
