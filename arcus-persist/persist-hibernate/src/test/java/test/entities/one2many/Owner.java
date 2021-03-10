package test.entities.one2many;

import io.sunshower.common.Identifier;
import io.sunshower.persistence.core.DistributableEntity;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@IdClass(Identifier.class)
@Table(name = "one2many_owner")
public class Owner {

  @Id private byte[] id;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
  private Set<Owned> owned;

  public Owner() {
    this.id = DistributableEntity.sequence.next().value();
  }

  public void addOwned(Owned owned) {
    if (this.owned == null) {
      this.owned = new HashSet<>();
    }
    owned.setOwner(this);
    this.owned.add(owned);
  }
}
