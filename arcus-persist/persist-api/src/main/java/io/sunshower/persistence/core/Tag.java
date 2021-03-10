package io.sunshower.persistence.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "TAG")
public class Tag extends DistributableEntity {

  @Column private String name;

  public Tag() {
    super();
  }

  public Tag(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
