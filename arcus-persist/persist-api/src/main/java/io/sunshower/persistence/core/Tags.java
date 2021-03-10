package io.sunshower.persistence.core;

import io.sunshower.persistence.core.converters.ClassConverter;
import javax.persistence.*;

@Entity
@Table(name = "ENTITY_TAGS")
public class Tags extends DistributableEntity {

  @Column(name = "entity_type")
  @Convert(converter = ClassConverter.class)
  private Class<?> type;

  @ManyToOne(cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn(name = "tag_id")
  private Tag tag;

  public Class<?> getType() {
    return type;
  }

  public void setType(Class<?> type) {
    this.type = type;
  }

  public Tag getTag() {
    return tag;
  }

  public void setTag(Tag tag) {
    this.tag = tag;
  }
}
