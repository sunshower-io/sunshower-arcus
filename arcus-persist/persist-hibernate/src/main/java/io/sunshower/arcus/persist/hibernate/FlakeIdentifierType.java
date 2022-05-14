package io.sunshower.arcus.persist.hibernate;

import io.sunshower.persistence.id.Identifier;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;

public class FlakeIdentifierType extends AbstractSingleColumnStandardBasicType<Identifier> {

  public static final FlakeIdentifierType INSTANCE = new FlakeIdentifierType();

  public FlakeIdentifierType() {
    super(FlakeBinaryTypeDescriptor.INSTANCE, FlakeSQLTypeDescriptor.INSTANCE);
  }

  @Override
  public String getName() {
    return "flake-binary";
  }

  @Override
  public String[] getRegistrationKeys() {
    return new String[] {this.getName(), "identifier", Identifier.class.getName()};
  }
}
