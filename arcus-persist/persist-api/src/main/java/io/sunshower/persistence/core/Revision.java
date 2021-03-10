package io.sunshower.persistence.core;

import io.sunshower.common.Identifier;
import io.sunshower.common.crypto.Multihash;
import io.sunshower.persist.Sequence;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "REVISION")
public class Revision extends AbstractEntity<Identifier> {

  @Id private Identifier id;

  @Basic
  @Column(name = "agent_id")
  private Identifier agentId;

  @Basic
  @Column(name = "entity_type")
  private int entityType;

  @Embedded private Multihash content;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "event_time")
  private Date eventTime;

  @Enumerated(EnumType.ORDINAL)
  private EventType type;

  @Embedded
  @AttributeOverride(name = "id", column = @Column(name = "entity_id"))
  private Identifier entityId;

  public Revision() {
    super(DistributableEntity.sequence.next());
  }

  @Override
  public Identifier getId() {
    return id;
  }

  @Override
  public void setId(Identifier id) {
    this.id = id;
  }

  public Identifier getAgentId() {
    return agentId;
  }

  public void setAgentId(Identifier agentId) {
    this.agentId = agentId;
  }

  public int getEntityType() {
    return entityType;
  }

  public void setEntityType(int entityType) {
    this.entityType = entityType;
  }

  public Identifier getEntityId() {
    return entityId;
  }

  public Multihash getContent() {
    return content;
  }

  public void setContent(Multihash content) {
    this.content = content;
  }

  public Date getEventTime() {
    return eventTime;
  }

  public void setEventTime(Date eventTime) {
    this.eventTime = eventTime;
  }

  public EventType getType() {
    return type;
  }

  public void setType(EventType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Revision{"
        + "agentId="
        + agentId
        + ", entityType="
        + entityType
        + ", eventTime="
        + eventTime
        + ", type="
        + type
        + ", entityId="
        + entityId
        + '}';
  }

  @Override
  public Identifier getIdentifier() {
    return getId();
  }

  @Override
  public Sequence<Identifier> getSequence() {
    return DistributableEntity.sequence;
  }

  public void setEntityId(Identifier id) {
    this.entityId = id;
  }
}
