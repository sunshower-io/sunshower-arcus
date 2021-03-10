package io.sunshower.persistence.core;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class NetworkAddress extends Address {

  public enum Type {
    IPV4,
    IPV6,
    DNS
  }

  public static transient AddressValidator validator;

  @Transient private transient Type version;

  public NetworkAddress() {}

  public NetworkAddress(char[] value) {
    super(value);
    checkValue();
  }

  public NetworkAddress(String chars) {
    super(chars);

    checkValue();
  }

  private void checkValue() {
    if (validator == null) {
      validator = new AddressValidator() {};
    }
    version = validator.check(value);
  }

  public Type getType() {
    return version;
  }
}
