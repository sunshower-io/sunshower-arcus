package io.sunshower.persist.hibernate;

import org.hibernate.boot.SessionFactoryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.boot.spi.SessionFactoryBuilderFactory;
import org.hibernate.boot.spi.SessionFactoryBuilderImplementor;

public class TypeRegistrationBuilderFactory implements SessionFactoryBuilderFactory {
  @Override
  public SessionFactoryBuilder getSessionFactoryBuilder(
      MetadataImplementor metadata, SessionFactoryBuilderImplementor defaultBuilder) {
    //    metadata.getTypeResolver().registerTypeOverride(FlakeIdentifierType.INSTANCE);
    //    return defaultBuilder;
    return defaultBuilder;
  }
}
