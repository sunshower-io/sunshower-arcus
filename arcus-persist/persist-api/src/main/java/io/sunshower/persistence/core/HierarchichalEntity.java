package io.sunshower.persistence.core;

import java.io.Serializable;
import java.util.*;
import javax.persistence.*;

@MappedSuperclass
public abstract class HierarchichalEntity<T extends Serializable, U extends Hierarchical<T, U>>
    extends AbstractEntity<T> implements Hierarchical<T, U> {

  @ManyToOne
  @JoinColumn(name = "parent_id")
  private U parent;

  protected HierarchichalEntity(T id) {
    super(id);
  }

  public Collection<U> getChildren() {
    if (children() == null) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(children());
  }

  public void setParent(U parent) {
    this.parent = parent;
  }

  @Override
  public U getParent() {
    return parent;
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean addChild(U child) {
    if (children() == null) {
      setChildren(new HashSet<>());
    }
    child.setParent((U) this);
    return children().add(child);
  }

  protected abstract Set<U> children();

  protected abstract void setChildren(Set<U> children);
}
