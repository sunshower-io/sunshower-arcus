package io.sunshower.arcus.config;

import java.io.IOException;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.val;
import org.junit.jupiter.api.Test;

class YAMLConfigurationReaderTest {

  @Test
  void printFormat() throws IOException {

    val persistence = new Persistence();
    val ds = List.of(createDataSource("a", "url"), createDataSource("b", "urlb"));
    persistence.datasources = ds;

    System.out.println("START\n\n");
    YAMLConfigurationReader.objectMapper()
        .writerFor(Persistence.class)
        .writeValues(System.out)
        .write(persistence);

    //        .writeValue(System.out, persistence);
    System.out.println("\n\nEND");
  }

  private DataSource createDataSource(String a, String url) {
    val ds = new DataSource();
    ds.modes = List.of(Mode.Read, Mode.Write);
    ds.url = url;
    ds.name = a;
    return ds;
  }

  enum Mode {
    @XmlEnumValue("read")
    Read,
    @XmlEnumValue("write")
    Write
  }

  @XmlRootElement(name = "data-sources")
  static class Persistence {

    @XmlElement(name = "data-sources")
    private List<DataSource> datasources;
  }

  @XmlRootElement(name = "data-source")
  static class DataSource {

    @XmlElement private String name;

    @XmlAttribute private String url;

    @XmlElement(type = Mode.class)
    private List<Mode> modes;
  }
}
