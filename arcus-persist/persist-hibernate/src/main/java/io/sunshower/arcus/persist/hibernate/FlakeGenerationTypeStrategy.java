package io.sunshower.arcus.persist.hibernate;

import io.sunshower.persistence.id.Identifiers;
import jakarta.persistence.GenerationType;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.id.factory.spi.GenerationTypeStrategy;
import org.hibernate.id.factory.spi.GeneratorDefinitionResolver;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.descriptor.java.JavaType;

public class FlakeGenerationTypeStrategy implements GenerationTypeStrategy {

  private final Map<Class<?>, IdentifierGenerator> sequences;

  public FlakeGenerationTypeStrategy() {
    sequences = new HashMap<>();
  }

  @Override
  public IdentifierGenerator createIdentifierGenerator(
      GenerationType generationType,
      String generatorName,
      JavaType<?> javaType,
      Properties config,
      GeneratorDefinitionResolver definitionResolver,
      ServiceRegistry serviceRegistry) {
    var generator = sequences.get(javaType.getJavaTypeClass());
    if (generator == null) {
      synchronized (sequences) {
        generator = sequences.get(javaType.getJavaTypeClass());
        if (generator == null) {
          generator = new FlakeIdentifierGenerator(Identifiers.newSequence(true));
          sequences.put(javaType.getJavaTypeClass(), generator);
        }
      }
    }
    return generator;
  }
}
