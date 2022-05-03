package io.sunshower.arcus.identicon.renderers.svg;

import static java.lang.String.format;

import io.sunshower.arcus.identicon.Point;
import io.sunshower.lang.primitives.Rope;
import java.util.Collection;
import lombok.val;

public final class SvgPath {

  private Rope shape;

  public SvgPath() {
    shape = new Rope();
  }

  private static int intValue(float v) {
    return (int) Math.floor(v);
  }

  public SvgPath addPoints(Collection<Point> points) {
    val piter = points.iterator();
    Rope pathStart = null;
    if (piter.hasNext()) {
      val start = piter.next();
      pathStart = new Rope(format("M%d %d", intValue(start.x), intValue(start.y)));
      while (piter.hasNext()) {
        pathStart = pathStart.append(format("L%d %d", intValue(start.x), intValue(start.y)));
      }
    }
    if (pathStart != null) {
      shape = pathStart.append("Z");
    }
    return this;
  }

  public SvgPath addCircle(Point center, float dia, boolean counterclockwise) {
    val sweep = counterclockwise ? 0 : 1;
    val radius = intValue(dia / 2);
    val diameter = intValue(dia);
    shape = shape.append(
        format("M%d %d",
            intValue(center.x),
            intValue(center.y + dia / 2F))
    ).append(
        format("a%d,%d 0 1,%d %d,0",
            radius,
            radius,
            sweep,
            diameter
        )
    ).append(
        format("a%d,%d 0 1,%d %d,0",
            radius,
            radius,
            sweep,
            -diameter
        )
    );
    return this;
  }

  public SvgPath addCircle(Point c, int diameter) {
    return addCircle(c, diameter, false);
  }

  public CharSequence getPath() {
    return shape.toString();
  }
}
