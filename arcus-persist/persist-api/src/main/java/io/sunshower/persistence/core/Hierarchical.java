package io.sunshower.persistence.core;

import java.util.Collection;

public interface Hierarchical<T, U extends Hierarchical<T, U>> {

  U getParent();

  Collection<U> getChildren();

  boolean addChild(U child);

  void setParent(U parent);
}
