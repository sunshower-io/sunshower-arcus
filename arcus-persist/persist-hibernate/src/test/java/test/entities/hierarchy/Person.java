package test.entities.hierarchy;

import io.sunshower.common.Identifier;
import io.sunshower.persist.Sequence;
import io.sunshower.persistence.core.DistributableEntity;
import io.sunshower.persistence.core.DistributableHierarchicalEntity;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "HIER_PERSON")
public class Person extends DistributableHierarchicalEntity<Person> {

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Set<Person> children;

  public Person() {}

  @Override
  protected Set<Person> children() {
    return children;
  }

  @Override
  protected void setChildren(Set<Person> children) {
    this.children = children;
  }

  @Override
  public Sequence<Identifier> getSequence() {
    return DistributableEntity.sequence;
  }
}
