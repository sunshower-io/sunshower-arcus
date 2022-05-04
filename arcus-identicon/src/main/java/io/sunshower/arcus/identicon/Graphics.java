package io.sunshower.arcus.identicon;

import static io.sunshower.arcus.identicon.Point.at;
import static java.lang.Math.floor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;

public class Graphics {

  private final Renderer renderer;
  private final Transformation transformation;

  public Graphics(Renderer renderer, @NonNull Transformation transformation) {
    this.renderer = renderer;
    this.transformation = transformation;
  }

  public Graphics(@NonNull Renderer renderer) {
    this(renderer, Transformations.IDENTITITY);
  }

  /**
   * add a polygon to this graphics context
   *
   * @param invert should we invert this polygon
   * @param points the points
   */
  public void addPolygon(boolean invert, Point... points) {
    val pts =
        (invert ? reverse(points) : List.of(points))
            .stream().map(transformation::transform).collect(Collectors.toList());
    renderer.addPolygon(pts);
  }

  public Graphics withNewTransformation(@NonNull Transformation transformation) {
    return new Graphics(renderer, transformation);
  }

  /** @param points the points to add. Does not invert */
  public void addPolygon(Point... points) {
    addPolygon(false, points);
  }

  public void addTriangle(boolean invert, float x, float y, float w, float h, float r) {
    var points = new ArrayList<>(List.of(at(x + w, y), at(x + w, y + h), at(x, y + h), at(x, y)));
    points.remove(((int) floor(r)) % 4);
    addPolygon(invert, points.toArray(new Point[0]));
  }

  public void addTriangle(float x, float y, float w, float h, float r) {
    addTriangle(false, x, y, w, h, r);
  }

  public void addRectangle(boolean invert, float x, float y, float w, float h) {
    addPolygon(invert, at(x, y), at(x + w, y), at(x + w, y + h), at(x, y + h));
  }

  public void addRectangle(float x, float y, float w, float h) {
    addPolygon(false, at(x, y), at(x + w, y), at(x + w, y + h), at(x, y + h));
  }

  public void addCircle(float x, float y, float s) {
    addCircle(false, x, y, s);
  }

  public void addCircle(boolean invert, float x, float y, float size) {
    val p = transformation.transform(at(x, y), size, size);
    renderer.addCircle(p, size, invert);
  }

  public void addRhombus(boolean invert, float x, float y, float w, float h) {
    addPolygon(
        invert, at(x + w / 2f, y), at(x + w, y + h / 2f), at(x + w / 2f, y + h), at(x, y + h / 2f));
  }

  public void addRhombus(float x, float y, float w, float h) {
    addRhombus(false, x, y, w, h);
  }

  private List<Point> reverse(Point[] points) {
    val results = new ArrayList<>(List.of(points));
    Collections.reverse(results);
    return results;
  }
}
