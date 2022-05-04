package io.sunshower.arcus.identicon;

import static io.sunshower.arcus.identicon.Point.at;

import lombok.ToString;
import lombok.val;

@ToString
public final class Transformation {

  final float x;
  final float y;
  final float size;
  final int rotation;

  public Transformation(float x, float y, float size, int rotation) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.rotation = rotation;
  }

  public Point transform(Point location, Float w, Float h) {
    val right = x + size;
    val bottom = y + size;
    val height = h != null ? h : 0f;
    val width = w != null ? w : 0f;
    if (rotation == 1) {
      return at(right - location.y - height, y + location.x);
    } else if (rotation == 2) {
      return at(right - location.x - width, bottom - location.y - height);
    } else if (rotation == 3) {
      return at(x + location.y, bottom - location.x - width);
    } else {
      return at(location.x + x, location.y + y);
    }
  }

  public Point transform(Point pt) {
    return transform(pt, null, null);
  }
}
