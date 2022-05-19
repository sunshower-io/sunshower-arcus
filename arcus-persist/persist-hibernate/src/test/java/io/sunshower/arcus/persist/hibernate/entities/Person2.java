package io.sunshower.arcus.persist.hibernate.entities;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "PEOPLE2")
public class Person2 {

  @Id
  @Getter
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
  @Column(name = "id", updatable = false, nullable = false)
  private UUID id;

  @Getter
  @Setter
  @Column(name = "first_name")
  private String firstname;

  @Getter
  @Setter
  @Column(name = "last_name")
  private String lastName;
}
