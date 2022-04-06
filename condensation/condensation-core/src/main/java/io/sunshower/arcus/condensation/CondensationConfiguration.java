package io.sunshower.arcus.condensation;

public interface CondensationConfiguration {

  boolean supports(String format);

  PropertyScanner getScanner();

  TypeInstantiator getInstantiator();

  boolean providesBinder();

  TypeBinder createBinder();
}
