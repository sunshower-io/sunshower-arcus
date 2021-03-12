package io.sunshower.arcus.persist.datasources;

import io.sunshower.arcus.persist.datasources.DataSources.Mode;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@XmlRootElement(name = "datasource-configuration")
public class DataSourceConfiguration {

  @Getter
  @Setter
  @XmlElement(name = "modes")
  private Set<Mode> modes;

  @Getter @Setter @XmlAttribute private String url;

  @Getter
  @Setter
  @XmlAttribute(name = "name")
  private String name;

  public DataSourceConfiguration() {
    this.modes = new HashSet<>();
  }
}
