package io.sunshower.arcus.persist.hibernate.entities;

import io.sunshower.persistence.id.Identifier;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PEOPLE")
public class Person {

  @Id
  @Getter
  @Column(columnDefinition = "binary(16)")
  @GeneratedValue(generator = "flake")
  private Identifier id;

  @Getter
  @Setter
  @Column(name = "first_name")
  private String firstname;

  @Getter
  @Setter
  @Column(name = "last_name")
  private String lastName;
}
