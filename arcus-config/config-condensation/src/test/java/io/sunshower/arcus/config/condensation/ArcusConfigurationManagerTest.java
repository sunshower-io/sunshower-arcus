package io.sunshower.arcus.config.condensation;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sunshower.arcus.condensation.Attribute;
import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.arcus.condensation.RootElement;
import io.sunshower.arcus.condensation.mappings.ReflectiveTypeInstantiator;
import io.sunshower.arcus.config.Configuration;
import io.sunshower.arcus.config.ConfigurationManager;
import io.sunshower.arcus.config.InMemoryConfigurationProvider;
import io.sunshower.arcus.config.Key;
import io.sunshower.arcus.reflect.Reflect;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ArcusConfigurationManagerTest {

  private Condensation condensation;
  private ConfigurationManager manager;
  private InMemoryConfigurationProvider configurationProvider;
  private ByteArrayOutputStream outputStream;
  private ByteArrayInputStream inputStream;

  @BeforeEach
  void setUp(@TempDir File tempdir) {
    outputStream = new ByteArrayOutputStream();
    condensation = Condensation.create("json");
    configurationProvider = new InMemoryConfigurationProvider();
    manager = new ArcusConfigurationManager(condensation, configurationProvider);
  }


  @Test
  @SneakyThrows
  void ensureReadingConfigurationWorks() {
    val config = """
         {
           "name": "hello",
           "value": "world"
        }
         """;

    @Configuration("test-name")
    @RootElement
    class Value {

      @Key("whatever.this.is")
      @Attribute
      String name;


      @Attribute
      String value;
    }
    registerInMemoryConfigurationProvider(Value.class, inputStream(config), outputStream);
    manager.read();

    val value = manager.getConfiguration(Value.class);
    assertTrue(value.getValue("test-name.whatever.this.is").isEmpty());
  }

  private <T> void registerInMemoryConfigurationProvider(Class<T> valueClass,
      InputStream inputStream, OutputStream outputStream) {
    configurationProvider.registerConfiguration(
        valueClass,
        () -> inputStream,
        () -> outputStream,
        "application/json");


  }

  private <T> void register(Class<T> type) {
    register(type, () -> Reflect.instantiate(type));
  }

  private <T> void register(Class<T> type, Supplier<T> instantiator) {
    ((ReflectiveTypeInstantiator) condensation.getInstantiator())
        .register(type, instantiator);
  }


  private InputStream inputStream(String source) {
    return (inputStream = new ByteArrayInputStream(source.getBytes(StandardCharsets.UTF_8)));
  }

}