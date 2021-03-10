package io.sunshower.persistence.core;

import java.io.Serializable;
import javax.persistence.*;

@MappedSuperclass
public abstract class AbstractEntity<T extends Serializable> implements Persistable<T> {

  protected AbstractEntity(T id) {
    setId(id);
    setDefaults();
  }

  protected void setDefaults() {}

  public abstract T getId();

  public abstract void setId(T id);

  public int hashCode() {
    T id = getId();
    if (id == null) {
      return 0;
    }
    return id.hashCode();
  }

  public boolean equals(Object o) {
    if (o == this) return true;
    if (o == null) return false;
    if (o.getClass().isAssignableFrom(getClass())) {
      return ((Persistable) o).getId().equals(getId());
    }
    return false;
  }
}
