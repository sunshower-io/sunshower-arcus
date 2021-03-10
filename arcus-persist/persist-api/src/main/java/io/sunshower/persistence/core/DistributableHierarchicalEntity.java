package io.sunshower.persistence.core;

import io.sunshower.common.Identifier;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.FieldBridge;

@MappedSuperclass
@XmlRootElement(name = "hierarchical-entity")
public abstract class DistributableHierarchicalEntity<T extends Hierarchical<Identifier, T>>
    extends HierarchichalEntity<Identifier, T> {

  @Id
  @DocumentId
  @Column(name = "id")
  @XmlAttribute(name = "id")
  @FieldBridge(impl = ByteArrayBridge.class)
  private Identifier id;

  protected DistributableHierarchicalEntity(Identifier id) {
    super(id);
  }

  protected DistributableHierarchicalEntity() {
    this(DistributableEntity.sequence.next());
  }

  @Override
  public Identifier getIdentifier() {
    return getId();
  }

  @Override
  public Identifier getId() {
    return id;
  }

  @Override
  public void setId(Identifier id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" + "id=" + id + '}';
  }
}
