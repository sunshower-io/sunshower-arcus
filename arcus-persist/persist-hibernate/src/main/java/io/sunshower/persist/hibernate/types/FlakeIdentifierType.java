package io.sunshower.persist.hibernate.types;

import io.sunshower.common.Identifier;
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

  public String[] getRegistrationKeys() {
    return new String[] {this.getName(), "identifier", Identifier.class.getName()};
  }
}
