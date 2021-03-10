package test.entities;

import io.sunshower.persistence.core.DistributableEntity;
import io.sunshower.persistence.core.converters.StringArrayConverter;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
@Entity
@Table(name = "SAMPLE_ENTITY", schema = "TEST_SCHEMA")
public class SampleEntity extends DistributableEntity {

  @Column(name = "vals")
  @Convert(converter = StringArrayConverter.class)
  private String[] values;

  @Column @XmlAttribute private String name;
}
