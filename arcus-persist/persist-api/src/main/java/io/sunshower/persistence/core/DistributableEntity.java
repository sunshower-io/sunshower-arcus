package io.sunshower.persistence.core;

import io.sunshower.common.Identifier;
import io.sunshower.common.rs.LaxIdentifierConverter;
import io.sunshower.persist.Sequence;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.val;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.FieldBridge;

@MappedSuperclass
@XmlRootElement(name = "entity")
public class DistributableEntity extends AbstractEntity<Identifier> {

  public static final Sequence<Identifier> sequence = Entities.getDefaultFlakeSequence();

  @Id
  @XmlID
  @DocumentId
  @Column(name = "id")
  @XmlAttribute(name = "id")
  @FieldBridge(impl = IdentifierBridge.class)
  @XmlJavaTypeAdapter(LaxIdentifierConverter.class)
  private Identifier id;

  public DistributableEntity() {
    super(null);
    val seq = getSequence();
    if (seq != null) {
      setId(seq.next());
    }
  }

  public DistributableEntity(Identifier id) {
    super(id);
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
  public Identifier getIdentifier() {
    return getId();
  }

  @Override
  public Sequence<Identifier> getSequence() {
    return sequence;
  }
}
