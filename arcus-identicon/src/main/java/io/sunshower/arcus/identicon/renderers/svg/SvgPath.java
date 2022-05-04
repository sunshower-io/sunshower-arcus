package io.sunshower.arcus.identicon.renderers.svg;

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

    System.out.printf("Rendering polygon: %s%n", points);
    val iter = points.iterator();
    if (iter.hasNext()) {
      val first = iter.next();
      var dataString = "M" + intValue(first.x) + " " + intValue(first.y);
      while (iter.hasNext()) {
        val next = iter.next();
        dataString += "L" + intValue(next.x) + " " + intValue(next.y);
      }
      shape.append(dataString + "Z");
    }
    return this;
  }

  @Override
  public SvgPath addCircle(Point center, float dia, boolean counterclockwise) {
    val sweep = counterclockwise ? 0 : 1;
    val radius = intValue(dia / 2);
    val diameter = intValue(dia);

    shape.append(
        "M" + intValue(center.x) + " " + intValue(center.y + diameter / 2f) +
        "a" + radius + "," + radius + " 0 1," + sweep + " " + diameter + ",0" +
        "a" + radius + "," + radius + " 0 1," + sweep + " " + (-diameter) + ",0"
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
