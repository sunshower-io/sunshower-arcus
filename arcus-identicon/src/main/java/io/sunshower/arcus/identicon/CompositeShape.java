package io.sunshower.arcus.identicon;

import java.util.ArrayList;
import java.util.List;
import lombok.val;

public class CompositeShape implements Shape {

  private final List<Shape> shapes;

  public CompositeShape() {
    shapes = new ArrayList<>();
  }

  @Override
  public void draw(Graphics graphics, float cell, int index) {
    for (val shape : shapes) {
      shape.draw(graphics, cell, index);
    }
  }

  public CompositeShape add(Shape shape) {
    shapes.add(shape);
    return this;
  }
}
