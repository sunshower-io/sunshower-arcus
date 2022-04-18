package io.sunshower.arcus.condensation.json;

import io.sunshower.arcus.condensation.CondensationConfiguration;
import io.sunshower.arcus.condensation.PropertyScanner;
import io.sunshower.arcus.condensation.TypeBinder;
import io.sunshower.arcus.condensation.TypeInstantiator;
import io.sunshower.arcus.condensation.json.JsonValue.Type;
import io.sunshower.arcus.condensation.mappings.AnnotationDrivenPropertyScanner;
import io.sunshower.arcus.condensation.mappings.CachingDelegatingTypeInstantiator;
import io.sunshower.arcus.condensation.mappings.ReflectiveTypeInstantiator;

public class JsonCondensationConfiguration implements CondensationConfiguration {

  private static final String JSON = "json";
  private final ReflectiveTypeInstantiator instantiator;
  private final AnnotationDrivenPropertyScanner scanner;
  private final JsonTypeBinder binder;

  public JsonCondensationConfiguration() {
    instantiator = new ReflectiveTypeInstantiator();
    scanner =
        new AnnotationDrivenPropertyScanner(new CachingDelegatingTypeInstantiator(instantiator));
    binder = new JsonTypeBinder(scanner);
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
    return true;
  }

  @Override
  public TypeBinder<Type> createBinder() {
    return new JsonTypeBinder(scanner);
  }
}
