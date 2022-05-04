package io.sunshower.arcus.identicon.renderers.svg;

import static java.lang.String.format;

import io.sunshower.arcus.identicon.Color;
import io.sunshower.arcus.identicon.Path;
import io.sunshower.arcus.identicon.Point;
import java.util.Collection;
import lombok.val;

final class SvgPath implements Path {

  private final Color color;
  private final float alpha;
  private final StringBuilder shape;

  public SvgPath(Color color, float alpha) {
    this.alpha = alpha;
    this.color = color;
    this.shape = new StringBuilder();
  }


  private static int intValue(float v) {
    return (int) Math.floor(v);
  }

  @Override
  public Color getColor() {
    return color;
  }

  @Override
  public float getAlpha() {
    return alpha;
  }

  @Override
  public SvgPath addPoints(Collection<Point> points) {

    val iter = points.iterator();
    if (iter.hasNext()) {
      val first = iter.next();
      shape.append(format("M%d %d", intValue(first.x), intValue(first.y)));
      while (iter.hasNext()) {
        val next = iter.next();
        shape.append(format("L%d %d", intValue(next.x), intValue(next.y)));
      }
      shape.append("Z");
    }
    return this;
  }

  @Override
  public SvgPath addCircle(Point center, float dia, boolean counterclockwise) {
    val sweep = counterclockwise ? 0 : 1;
    val radius = intValue(dia / 2);
    val diameter = intValue(dia);
    shape.append(
        format("M%d %d",
            intValue(center.x),
            intValue(center.y + diameter / 2f))
    ).append(
        format(
            "a%d,%d 0 1, %d %d,0",
            radius,
            radius,
            sweep,
            diameter
        )
    ).append(
        format(
            "a%d,%d 0 1, %d %d,0",
            radius,
            radius,
            sweep,
            -diameter
        )
    );
    return this;
  }

  @Override
  public SvgPath addCircle(Point c, int diameter) {
    return addCircle(c, diameter, false);
  }

  @Override
  public CharSequence getPath() {
    return shape.toString();
  }

}
