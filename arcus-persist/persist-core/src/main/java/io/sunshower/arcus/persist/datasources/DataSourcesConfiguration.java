package io.sunshower.arcus.persist.datasources;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@XmlRootElement(name = "data-sources")
public class DataSourcesConfiguration {

  @Getter
  @Setter
  @XmlElement(name = "data-sources")
  private List<DataSourceConfiguration> datasources;

  public DataSourcesConfiguration() {
    datasources = new ArrayList<>();
  }


}
