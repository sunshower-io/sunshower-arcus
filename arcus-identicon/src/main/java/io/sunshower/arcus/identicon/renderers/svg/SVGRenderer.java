package io.sunshower.arcus.identicon.renderers.svg;

import io.sunshower.arcus.identicon.Color;
import io.sunshower.arcus.identicon.IconWriter;
import io.sunshower.arcus.identicon.Path;
import io.sunshower.arcus.identicon.Point;
import io.sunshower.arcus.identicon.Renderer;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class SVGRenderer implements Renderer {

  static final Pattern RGBA_PATTERN = Pattern.compile("^(#......)(..)?$");

  private final IconWriter writer;
  private final Map<Color, Path> paths;

  public SVGRenderer(IconWriter writer) {
    this.writer = writer;
    this.paths = new HashMap<>();
  }

  @Override
  public void endShape() {

  }

  @Override
  public void beginShape(Color color, float alpha) {

  }

  @Override
  public void setBackgroundColor(Color color, float alpha) {

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

  @Override
  public void addPolygon(List<Point> pts) {

  }

  @Override
  public void finish() {

  }
}
