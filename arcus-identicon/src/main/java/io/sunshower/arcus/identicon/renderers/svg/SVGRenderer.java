package io.sunshower.arcus.identicon.renderers.svg;

import io.sunshower.arcus.identicon.Color;
import io.sunshower.arcus.identicon.IconWriter;
import io.sunshower.arcus.identicon.Path;
import io.sunshower.arcus.identicon.Point;
import io.sunshower.arcus.identicon.Renderer;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import lombok.val;

public class SVGRenderer implements Renderer {

  static final Pattern RGBA_PATTERN = Pattern.compile("^(#......)(..)?$");
  private final IconWriter writer;
  private final Map<Color, Path> paths;
  private final float opacity;
  private Path currentPath;

  public SVGRenderer(IconWriter writer, float opacity) {
    this.writer = writer;
    this.opacity = opacity;
    this.paths = new LinkedHashMap<>();
  }

  @Override
  public void endShape() {

  }

  @Override
  public void beginShape(Color color, float alpha) {
    this.currentPath = paths.computeIfAbsent(color, k -> new SvgPath(color, alpha));
  }

  @Override
  public void setBackgroundColor(Color color, float alpha) {
    writer.setBackground(color, alpha);
  }

  @Override
  public void addPoint(Point point) {
    addPoints(Set.of(point));
  }

  @Override
  public void addPoints(Collection<Point> points) {
    check();
    currentPath.addPoints(points);
  }


  @Override
  public void addCircle(Point center, float diameter, boolean counterclockwise) {
    check();
    currentPath.addCircle(center, diameter, counterclockwise);
  }

  @Override
  public void addPolygon(List<Point> pts) {
    addPoints(pts);
  }

  @Override
  public void finish() {

    for(val kv : paths.entrySet()) {
      writer.addPath(kv.getValue());
    }

  }

  private void check() {
    if(currentPath == null) {
      throw new IllegalStateException("Error: current path is null");
    }
  }
}
