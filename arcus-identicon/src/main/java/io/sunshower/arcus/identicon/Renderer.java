package io.sunshower.arcus.identicon;

import java.util.Collection;

public interface Renderer {


  /**
   * terminate a shape
   */
  void endShape();

  /**
   * begin a shape
   * @param color the color of the shape
   * @param alpha the alpha
   */
  void beginShape(Color color, float alpha);
  default void beginShape(Color color) {
    beginShape(color, 1);
  }

  /**
   * set the background color of this polygon
   * @param color the color
   * @param alpha the alpha
   */
  void setBackgroundColor(Color color, float alpha);

  /**
   * set the background color
   * @param color
   */
  default void setBackgroundColor(Color color) {
    setBackgroundColor(color, 1);
  }


  void addPoint(Point point);

  void addPoints(Collection<Point> points);


  /**
   *
   * @param center the center of this circle
   * @param diameter the diameter of this circle
   * @param counterclockwise true if we should draw it counterclockwise
   */
  void addCircle(Point center, float diameter, boolean counterclockwise);


  /**
   * @param center the center of the circle
   * @param diameter the diameter of the circle
   */
  default void addCircle(Point center, float diameter) {
    addCircle(center, diameter, false);
  }



}
