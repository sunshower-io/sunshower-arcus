package test.entities.one2many;

import java.util.UUID;
import javax.persistence.*;

@Entity
@Table(name = "one2many_owned")
public class Owned {

  @Id private UUID id;

  @ManyToOne private Owner owner;

  public Owned() {
    this.id = UUID.randomUUID();
  }

  public UUID getId() {
    return id;
  }

  public Owner getOwner() {
    return owner;
  }

  public void setOwner(Owner owner) {
    this.owner = owner;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
