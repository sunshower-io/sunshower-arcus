package test.entities.one2one;

import io.sunshower.persistence.core.DistributableEntity;
import javax.persistence.*;

@Entity
@Table(name = "OWNER")
@Access(AccessType.FIELD)
public class Owner extends DistributableEntity {

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumns({
    @JoinColumn(
      name = "ownee_id",
      insertable = false,
      updatable = false,
      referencedColumnName = "id"
    )
  })
  private Ownee ownee;

  @Basic private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Ownee getOwnee() {
    return ownee;
  }

  public void setOwnee(Ownee ownee) {
    ownee.setOwner(this);
    this.ownee = ownee;
  }
}
