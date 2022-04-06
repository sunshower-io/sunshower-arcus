package com.aire.ux.condensation;

import com.aire.ux.condensation.mappings.AnnotationDrivenPropertyScanner;
import com.aire.ux.condensation.mappings.CachingDelegatingTypeInstantiator;
import com.aire.ux.condensation.mappings.DefaultTypeBinder;
import com.aire.ux.condensation.mappings.ReflectiveTypeInstantiator;

public class DefaultCondensationConfiguration implements CondensationConfiguration {

  private static final String JSON = "json";
  private final ReflectiveTypeInstantiator instantiator;
  private final AnnotationDrivenPropertyScanner scanner;
  private final DefaultTypeBinder binder;

  public DefaultCondensationConfiguration() {
    instantiator = new ReflectiveTypeInstantiator();
    scanner =
        new AnnotationDrivenPropertyScanner(new CachingDelegatingTypeInstantiator(instantiator));
    binder = new DefaultTypeBinder(scanner);
  }

  @Override
  public boolean supports(String format) {
    return JSON.equals(format);
  }

  @Override
  public PropertyScanner getScanner() {
    return scanner;
  }

  @Override
  public TypeInstantiator getInstantiator() {
    return instantiator;
  }

  @Override
  public boolean providesBinder() {
    return false;
  }

  @Override
  public TypeBinder createBinder() {
    throw new UnsupportedOperationException("not supported");
  }
}
