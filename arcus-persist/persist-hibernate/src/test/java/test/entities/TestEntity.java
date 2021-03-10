package test.entities;

import io.sunshower.persistence.core.DistributableEntity;
import io.sunshower.persistence.core.MachineAddress;
import io.sunshower.persistence.core.NetworkAddress;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
@Table(name = "TestEntity")
public class TestEntity extends DistributableEntity {

  @ManyToOne
  @JoinColumns({@JoinColumn(name = "parent_id", insertable = false, updatable = false)})
  private TestEntity parent;

  @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
  private Set<TestEntity> children;

  @Basic private MachineAddress mac;

  @Basic private NetworkAddress inet;

  @Column private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public TestEntity getParent() {
    return parent;
  }

  public Set<TestEntity> getChildren() {
    return children;
  }

  public void addChild(TestEntity entity) {
    if (this.children == null) {
      this.children = new HashSet<>();
    }
    entity.parent = this;
    this.children.add(entity);
  }

  public MachineAddress getMac() {
    return mac;
  }

  public void setMac(MachineAddress mac) {
    this.mac = mac;
  }

  public NetworkAddress getInet() {
    return inet;
  }

  public void setInet(NetworkAddress inet) {
    this.inet = inet;
  }

  public void setParent(TestEntity parent) {
    this.parent = parent;
  }

  public void setChildren(Set<TestEntity> children) {
    this.children = children;
  }
}
