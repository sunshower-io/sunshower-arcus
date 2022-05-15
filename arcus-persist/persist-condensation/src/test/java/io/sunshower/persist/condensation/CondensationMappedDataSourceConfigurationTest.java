package io.sunshower.persist.condensation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.persistence.config.DataSourceConfiguration.Mode;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

class CondensationMappedDataSourceConfigurationTest {

  static final String document =
      "{\n"
          + "  \"scanned-packages\": [\n"
          + "    \"hello.world\",\n"
          + "    \"how.are.you\"\n"
          + "  ],\n"
          + "  \"driver-class\": \"org.hsqldb.Driver\",\n"
          + "  \"username\": \"test\",\n"
          + "  \"password\": \"hello\",\n"
          + "  \"mode\": \"WriteOnly\",\n"
          + "  \"additional-properties\": null,\n"
          + "  \"connection-string\": null\n,"
          + "  \"migration-locations\": null\n"
          + "}\n";

  @Test
  @SneakyThrows
  void ensureWritingWorks() {
    val cfg = new CondensationMappedDataSourceConfiguration();
    cfg.setMode(Mode.WriteOnly);

    cfg.setPassword("hello");
    cfg.setUsername("test");
    cfg.setScannedPackages(new String[] {"hello.world", "how.are.you"});
    cfg.setDriverClassName("org.hsqldb.Driver");

    val result = Condensation.write("json", CondensationMappedDataSourceConfiguration.class, cfg);
    assertEquals(document.replaceAll("\\s+", ""), result);
  }

  @Test
  void ensureReadingDocumentWorks() {
    val doc =
        Condensation.create("json").read(CondensationMappedDataSourceConfiguration.class, document);
    assertEquals("hello", doc.getPassword());
    assertEquals(Mode.WriteOnly, doc.getMode());
  }
}
