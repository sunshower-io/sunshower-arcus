package io.sunshower.arcus.identicon;

import java.util.Collection;

public interface Path {

  Path addPoints(Collection<Point> points);

  Path addCircle(Point center, float dia, boolean counterclockwise);

  Path addCircle(Point c, int diameter);

  CharSequence getPath();

  Path add(Renderer renderer);
}
