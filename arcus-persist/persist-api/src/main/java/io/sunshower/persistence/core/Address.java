package io.sunshower.persistence.core;

import java.util.Objects;

public abstract class Address {

  protected String value;

  protected Address() {}

  protected Address(char[] value) {
    Objects.requireNonNull(value);
    this.value = new String(value);
  }

  protected Address(String value) {
    this.value = value;
  }

  public String toString() {
    return value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Address)) return false;
    Address that = (Address) o;
    return Objects.equals(value, that.value);
  }

  @Override
  public int hashCode() {
    return value == null ? 0 : value.hashCode();
  }

  public String getValue() {
    return value;
  }
}
