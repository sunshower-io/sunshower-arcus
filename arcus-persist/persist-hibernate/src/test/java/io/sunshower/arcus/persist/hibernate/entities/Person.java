package io.sunshower.arcus.persist.hibernate.entities;


import io.sunshower.persistence.id.Identifier;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PEOPLE")
public class Person {

  @Id
  @Getter
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
