package com.aire.ux.test;

import lombok.val;

public class NodeHierarchyWalker<T> {

  public void walk(T initial, NodeAdapter<T> adapter, NodeVisitor<T> visitor) {
    visitor.openNode(initial, adapter);
    for (val c : adapter.getChildren(initial)) {
      walk(c, adapter, visitor);
    }
    visitor.closeNode(initial, adapter);
  }
}
