package io.sunshower.arcus.config;

import static io.sunshower.arcus.config.ConfigurationLoader.load;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.sunshower.arcus.logging.Logging;
import java.io.File;
import java.io.StringReader;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.val;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.Test;

public class ConfigurationLoaderTest {

  static {
    Logging.setLevel("io.sunshower.arcus.config", Level.ALL);
  }

  @Test
  void ensureConfigurationLoaderCanLoadConfigurationFromYaml() throws Exception {

    val cfg = """
        value: "hello!"
        """.stripIndent();

    val result = load(Cfg.class, new StringReader(cfg), "application/x-yaml");
    assertEquals(result.value, "hello!");
  }

  @Test
  void ensureDetectingMimeTypeWorksForYamlFile() throws Exception {
    val file = new File(ClassLoader.getSystemResource("test-mimetype.yaml").getFile());
    assertEquals(load(Cfg.class, file).value, "hello");
  }

  @Test
  void ensureComplexValuesAreLoaded() throws Exception {
    val file = new File(ClassLoader.getSystemResource("test-complex.yaml").getFile());
    val result = load(Persistence.class, file);


  }

  enum Mode {
    @XmlEnumValue("read")
    Read,
    @XmlEnumValue("write")
    Write

  }

  @XmlRootElement(name = "persistence")
  @XmlAccessorType(XmlAccessType.NONE)
  static class Persistence {

    @XmlElement(name = "data-sources")
    private List<DataSource> datasources;

  }

  @XmlRootElement(name = "data-source")
  static class DataSource {

    @JsonInclude
    @XmlElement(type = Mode.class)
    private List<Mode> modes;

    @JsonInclude
    @XmlElement
    private String name;

    @JsonInclude
    @XmlAttribute
    private String url;
  }

  @XmlRootElement(name = "snake-case")
  static class Cfg {

    @XmlAttribute(name = "value")
    private String value;
  }
}
