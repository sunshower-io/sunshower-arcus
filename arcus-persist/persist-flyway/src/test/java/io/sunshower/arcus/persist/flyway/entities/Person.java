package io.sunshower.arcus.persist.flyway.entities;

import io.sunshower.persistence.id.Identifier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
