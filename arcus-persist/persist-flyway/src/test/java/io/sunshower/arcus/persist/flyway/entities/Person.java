package io.sunshower.arcus.persist.flyway.entities;

import io.sunshower.persistence.id.Identifier;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "PEOPLE")
public class Person {

  @Id
  @Getter
  @Column(name = "id")
  @GeneratedValue(generator = "flake")
  private Identifier id;
}
