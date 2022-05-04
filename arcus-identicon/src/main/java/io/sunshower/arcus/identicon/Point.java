package io.sunshower.arcus.identicon;

import lombok.Getter;
import lombok.ToString;

@ToString
public final class Point {
  @Getter public final float x;
  @Getter public final float y;

  public Point(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public static Point at(float x, float y) {
    return new Point(x, y);
  }
}
