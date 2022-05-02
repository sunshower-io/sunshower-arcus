package io.sunshower.arcus.config.condensation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.condensation.Attribute;
import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.arcus.condensation.RootElement;
import io.sunshower.arcus.condensation.mappings.ReflectiveTypeInstantiator;
import io.sunshower.arcus.config.ConfigurationManager;
import io.sunshower.arcus.reflect.Reflect;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ArcusConfigurationManagerTest {

  private Condensation condensation;
  private ConfigurationManager manager;

  @BeforeEach
  void setUp(@TempDir File tempdir) {
    condensation = Condensation.create("json");
    manager = new ArcusConfigurationManager(condensation, tempdir);
  }


  @Test
  void ensureReadingConfigurationWorks() {
    val config = """
         {
           "name": "hello",
           "value": "world"
        }
         """;
    @RootElement
    class Value {

      @Attribute
      String name;
      @Attribute
      String value;
    }

    val value = manager.read(Value.class,
        new ByteArrayInputStream(config.getBytes(StandardCharsets.UTF_8)), "hello");
    assertEquals("hello", value.get().name);
  }

  private <T> void register(Class<T> type) {
    register(type, () -> Reflect.instantiate(type));
  }

  private <T> void register(Class<T> type, Supplier<T> instantiator) {
    ((ReflectiveTypeInstantiator) condensation.getInstantiator())
        .register(type, instantiator);
  }

}