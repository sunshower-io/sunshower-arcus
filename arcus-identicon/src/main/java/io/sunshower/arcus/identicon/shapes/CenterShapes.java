package io.sunshower.arcus.identicon.shapes;

import static io.sunshower.arcus.identicon.Point.at;
import static java.lang.Math.floor;

import io.sunshower.arcus.identicon.CompositeShape;
import lombok.val;

public class CenterShapes extends CompositeShape {


  public CenterShapes() {

    /**
     * polygon
     */
    add((graphics, cell, index) -> {
      val k = cell * 0.42f;
      graphics.addPolygon(
          at(0f, 0f),
          at(cell, 0f),
          at(cell, cell - k * 2f),
          at(cell - k, cell),
          at(0f, cell)
      );
    });

    /**
     * triangle
     */
    add((graphics, cell, index) -> {
      val w = (float) floor(cell * 0.5F);
      val h = (float) floor(cell * 0.8F);
      graphics.addTriangle(cell - w, 0f, w, h, 2f);
    });

    /**
     * rectangle
     */
    add((graphics, cell, index) -> {
      val s = (float) floor(cell / 3f);
      graphics.addRectangle(s, s, cell - s, cell - s);
    });

    add((graphics, cell, index) -> {
      val innert = cell * 0.1F;

      val inner = (float) (innert > 1F ?
          floor(innert)
          : innert > 0.5F ? 1F : innert);

      val outer = (float) (cell < 6F ? 1F
          : cell < 8F ? 2F : floor(cell * 0.25f));
      graphics.addRectangle(outer, outer, cell - inner - outer, cell - inner - outer);
    });

    add((graphics, cell, index) -> {
      val m = (float) floor(cell * 0.15f);
      val s = (float) floor(cell * 0.5f);
      graphics.addCircle(cell - s - m, cell - s - m, s);
    });

    add((graphics, cell, index) -> {
      val inner = cell * 0.1f;
      val outer = inner * 4f;
      graphics.addRectangle(0f, 0f, cell, cell);
      graphics.addPolygon(
          true,
          at(outer, (float) floor(outer)),
          at(cell - inner, (float) floor(outer)),
          at(outer + (cell - outer - inner) / 2f, cell - inner));
    });

    add((graphics, cell, index) -> {
      graphics.addPolygon(
          at(0f, 0f),
          at(cell, 0f),
          at(cell, cell * 0.7f),
          at(cell * 0.4f, cell * 0.4f),
          at(cell * 0.7f, cell),
          at(0f, cell)
      );
    });

    add((graphics, cell, index) -> {
      graphics.addTriangle(cell / 2f, cell / 2f, cell / 2f, cell / 2f, 3f);
    });

    add((graphics, cell, index) -> {
      graphics.addRectangle(0f, 0f, cell, cell / 2f);
      graphics.addRectangle(0f, cell / 2f, cell / 2f, cell / 2f);
      graphics.addTriangle(cell / 2f, cell / 2f, cell / 2f, cell / 2f, 1f);
    });

    add((graphics, cell, index) -> {
      val innert = cell * 0.14f;
      val inner = (float) (cell < 8F ? innert : floor(innert));

      val outer = (float) (cell < 4F ? 1F
          : cell < 6F ? 2F : floor(cell * 0.35F));
      graphics.addRectangle(0f, 0f, cell, cell);
      graphics.addRectangle(true, outer, outer, cell - outer - inner, cell - outer - inner);
    });

    add((graphics, cell, index) -> {
      val inner = cell * 0.12f;
      val outer = inner * 3f;
      graphics.addRectangle(0f, 0f, cell, cell);
      graphics.addCircle(true, outer, outer, cell - inner - outer);
    });

    add((graphics, cell, index) -> graphics.addTriangle(cell / 2f, cell / 2f, cell / 2f, cell / 2f,
        3f));

    add((graphics, cell, index) -> {
      val m = cell * 0.25f;
      graphics.addRectangle(0f, 0f, cell, cell);
      graphics.addRhombus(true, m, m, cell - m, cell - m);
    });

    add((graphics, cell, index) -> {
      val m = cell * 0.4f;
      val s = cell * 1.2f;
      if (index == 0) {
        graphics.addCircle(m, m, s);
      }
    });
  }
}
