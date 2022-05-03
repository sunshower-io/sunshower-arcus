package io.sunshower.arcus.identicon.renderers.svg;

import io.sunshower.arcus.identicon.Color;
import io.sunshower.arcus.identicon.Point;
import io.sunshower.arcus.identicon.Renderer;
import java.util.Collection;
import java.util.regex.Pattern;

public class SVGRenderer implements Renderer {

  static final Pattern RGBA_PATTERN = Pattern.compile("^(#......)(..)?$");

  @Override
  public void endShape() {

  }

  @Override
  public void beginShape(Color color) {

  }

  @Override
  public void setBackgroundColor(Color color) {

  }

  @Override
  public void addPoint(Point point) {

  }

  @Override
  public void addPoints(Collection<Point> points) {

  }

  @Override
  public void addCircle(Point center, float diameter, boolean counterclockwise) {

  }
}
