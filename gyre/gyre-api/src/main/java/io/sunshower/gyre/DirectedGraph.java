package io.sunshower.gyre;

import java.util.Set;

public interface DirectedGraph<E, V> extends Graph<DirectedGraph.Edge<E>, V> {

  byte IN_FLAG = 0x1;
  byte OUT_FLAG = IN_FLAG << 1;

  static <E> Edge<E> incoming(E label) {
    return new DirectedEdge<>(label, Direction.Incoming);
  }

  static <E> Edge<E> outgoing(E label) {
    return new DirectedEdge<>(label, Direction.Outgoing);
  }

  enum Direction {
    Incoming(IN_FLAG),
    Outgoing(OUT_FLAG);

    final byte value;

    Direction(byte value) {
      this.value = value;
    }

    public byte clear(byte v) {
      return (byte) (v & ~value);
    }

    public byte set(byte v) {
      return (byte) (v | value);
    }

    public boolean is(byte dir) {
      return (dir & value) == value;
    }

    public static boolean is(byte flag, Direction d) {
      return d.is(flag);
    }
  }

  interface Edge<E> {
    E getLabel();

    Direction getDirection();
  }

  final class DirectedEdge<E> implements Edge<E> {
    final E value;
    final Direction direction;

    public DirectedEdge(E value, Direction direction) {
      this.value = value;
      this.direction = direction;
    }

    public E getLabel() {
      return value;
    }

    public Direction getDirection() {
      return direction;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (!(o instanceof DirectedGraph.Edge)) return false;

      Edge<?> edge = (Edge<?>) o;

      if (value != null ? !value.equals(edge.getLabel()) : edge.getLabel() != null) return false;
      return direction == edge.getDirection();
    }

    @Override
    public int hashCode() {
      int result = value != null ? value.hashCode() : 0;
      result = 31 * result + direction.hashCode();
      return result;
    }

    @Override
    public String toString() {
      return String.format("E[%s:%s]", value, getDirection());
    }
  }

  boolean containsEdge(V source, V target, Direction d);

  Set<E> adjacentEdges(V vertex, Direction direction);

  int degreeOf(V vertex, Direction direction);

  DirectedGraph<E, V> clone();
}
