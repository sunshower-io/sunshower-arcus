package io.sunshower.arcus.identicon;

@FunctionalInterface
public interface Shape {

  default Shape subshape(int idx) {
    return this;
  }

  void draw(Graphics graphics, float cell, int index);

  default void draw(Graphics graphics, float cell) {
    draw(graphics, cell, 0);
  }

  default int subshapeCount() {
    return 0;
  }
}
