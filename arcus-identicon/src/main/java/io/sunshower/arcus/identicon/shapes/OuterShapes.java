package io.sunshower.arcus.identicon.shapes;

import io.sunshower.arcus.identicon.CompositeShape;

public class OuterShapes extends CompositeShape {

  public OuterShapes() {
    add((graphics, cell, index) -> graphics.addTriangle(0f, 0f, cell, cell, 0f));
    add(((graphics, cell, index) -> graphics.addTriangle(0f, cell / 2f, cell, cell / 2f, 0f)));
    add(((graphics, cell, index) -> graphics.addRhombus(0f, 0f, cell, cell)));
    add(((graphics, cell, index) -> {
      var m = cell / 6f;
      graphics.addCircle(m, m, cell - 2 * m);
    }));
  }

}
