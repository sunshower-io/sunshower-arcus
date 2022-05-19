package io.sunshower.arcus.persist.hibernate;

import java.util.Collections;
import java.util.Map;
import org.hibernate.jpa.spi.IdentifierGeneratorStrategyProvider;

public class FlakeIdentifierGeneratorStrategyProvider implements
    IdentifierGeneratorStrategyProvider {

  @Override
  public Map<String, Class<?>> getStrategies() {
    return Collections.singletonMap("flake", FlakeGenerationTypeStrategy.class);
  }
}
