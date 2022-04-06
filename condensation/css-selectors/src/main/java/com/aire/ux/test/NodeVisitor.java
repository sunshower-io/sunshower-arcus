package com.aire.ux.test;

public interface NodeVisitor<T> {

  public void openNode(T node, NodeAdapter<T> adapter);

  public void closeNode(T node, NodeAdapter<T> adapter);
}
