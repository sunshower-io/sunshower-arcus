package io.sunshower.arcus.lang.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import io.sunshower.lang.Environment;
import java.util.Map;
import lombok.val;
import org.junit.jupiter.api.Test;

class EnvironmentManagerTest {

  @Test
  void ensurePropertiesApiMakesSense() {
    val key =
        "dfgqpwetu13049t5uqgyhasdgqwelhqlwdfhqwf"; // avoid collisions with users' environments
    EnvironmentManager.props()
        .key(key)
        .value("world")
        .key(key + 4)
        .value("sup")
        .with(
            e -> {
              assertEquals(e.getSystemProperty(key), "world");
              assertEquals(e.getSystemProperty(key + 4), "sup");
            });
    assertNull(Environment.getDefault().getSystemProperty(key));
    assertNull(Environment.getDefault().getSystemProperty(key + 4));
  }

  @Test
  void ensurePropertiesModificationWorks() {
    val key =
        "dfgqpwetu13049t5uqgyhasdgqwelhqlwdfhqwf"; // avoid collisions with users' environments
    val map = Map.of(key, "world");
    EnvironmentManager.withSystemProperties(
        map,
        e -> {
          assertEquals(e.getSystemProperty(key), "world");
        });
    assertNull(Environment.getDefault().getSystemProperty(key));
  }

  @Test
  void ensureEnvironmentModificationWorks() {
    val key =
        "dfgqpwetu13049t5uqgyhasdgqwelhqlwdfhqwf"; // avoid collisions with users' environments
    val map = Map.of(key, "world");
    EnvironmentManager.withEnvironment(
        map,
        e -> {
          assertEquals(e.getEnvironmentVariable(key), "world");
        });
    assertNull(Environment.getDefault().getEnvironmentVariable(key));
  }

  @Test
  void ensureEnvironmentAPIMakesSense() {
    val key = "dfgqpwetu13049t5uqgyhasdgqwelhqlwdfhqwf";
    EnvironmentManager.env()
        .key(key)
        .value("coolbeans")
        .key(key + 2)
        .value("beansbeans")
        .with(
            e -> {
              assertEquals(e.getEnvironmentVariable(key), "coolbeans");
              assertEquals(e.getEnvironmentVariable(key + 2), "beansbeans");
            });

    assertNull(Environment.getDefault().getEnvironmentVariable(key));
  }
}
