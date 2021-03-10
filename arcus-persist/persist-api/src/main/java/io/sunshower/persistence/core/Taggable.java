package io.sunshower.persistence.core;

import java.util.Set;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public interface Taggable {

  Set<Tags> getTags();

  default void addTag(Tag tag) {
    final Tags t = new Tags();
    t.setType(getType());
    t.setTag(tag);
    getTags().add(t);
  }

  default Class<?> getType() {
    return getClass();
  }

  boolean hasTag(Tag tag);
}
