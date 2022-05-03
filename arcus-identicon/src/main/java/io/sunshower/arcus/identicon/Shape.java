package io.sunshower.arcus.identicon;

@FunctionalInterface
public interface Shape {


  void draw(Graphics graphics, float cell, int index);

  default void draw(Graphics graphics, float cell) {
    draw(graphics, cell, 0);
  }

}
