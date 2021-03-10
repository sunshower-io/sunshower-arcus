package io.sunshower.persistence.core;

import javax.persistence.Embeddable;

@Embeddable
public class MachineAddress extends Address {

  public MachineAddress(char[] value) {
    super(value);
  }

  public MachineAddress(String value) {
    super(value);
  }

  public MachineAddress() {}
}
