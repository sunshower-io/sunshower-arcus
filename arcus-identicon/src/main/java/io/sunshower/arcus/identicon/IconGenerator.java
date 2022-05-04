package io.sunshower.arcus.identicon;

import static java.lang.Math.floor;

import java.util.ArrayList;
import java.util.List;
import lombok.val;

public class IconGenerator {

  static final int[][] INNER = {
    {1, 1},
    {2, 1},
    {2, 2},
    {1, 2}
  };
  static final int[][] OUTER_1 =
      new int[][] {
        {1, 0},
        {2, 0},
        {2, 3},
        {1, 3},
        {0, 1},
        {3, 1},
        {3, 2},
        {0, 2}
      };
  static final int[][] OUTER_2 =
      new int[][] {
        {0, 0},
        {3, 0},
        {3, 3},
        {0, 3}
      };
  private final float x;
  private final float y;
  private final double size;
  private final double padding;
  private final int cellSize;
  private final Graphics graphics;
  private final Renderer renderer;
  private final Configuration configuration;

  public IconGenerator(Renderer renderer, Configuration configuration) {
    this.renderer = renderer;
    this.configuration = configuration;
    this.padding = (int) floor(configuration.getSize() * configuration.getPadding());
    this.size = configuration.getSize() - padding * 2F;
    this.graphics = new Graphics(renderer);
    this.cellSize = (int) floor(this.size / 4);
    final double floor = floor(padding + size / 2f - cellSize * 2f);
    this.x = (float) (configuration.getX() + floor);
    this.y = (float) (configuration.getY() + floor);
  }

  private static int indexOf(
      CharSequence sequence, List<Integer> colorIndexes, Color[] availableColors, int i) {
    return Integer.parseInt(String.valueOf(sequence.charAt(8 + i)), 16) % availableColors.length;
  }

  public void apply(CharSequence sequence) {
    System.out.println("Rendering " + sequence);
    render(sequence, 0, Shapes.outer, 2, 3, OUTER_1);
    render(sequence, 1, Shapes.outer, 4, 5, OUTER_2);
    render(sequence, 2, Shapes.inner, 1, -1, INNER);
    renderer.finish();
  }

  private void render(
      CharSequence sequence,
      int colorIndex,
      Shape shape,
      int index,
      int rotationIndex,
      int[][] positions) {
    val r = rotationIndex != -1 ? hex(sequence.charAt(rotationIndex)) : 0;

    val subshape = shape.subshape((hex(sequence.charAt(index))) % shape.subshapeCount());
    val hue = computeHue(sequence);
    val colors = configuration.colors(hue);
    val colorIndexes = calculateColorIndexes(colors, sequence);
    doRender(colors[colorIndexes.get(colorIndex)], subshape, r, positions);
  }

  private void doRender(Color color, Shape shape, int r, int[][] positions) {
    renderer.beginShape(color, configuration.getOpacity());
    for (int i = 0; i < positions.length; i++) {
      val g =
          graphics.withNewTransformation(
              new Transformation(
                  x + positions[i][0] * cellSize,
                  y + positions[i][1] * cellSize,
                  cellSize,
                  r++ % 4));
      shape.draw(g, cellSize, i);
    }
    renderer.endShape();
  }

  private List<Integer> calculateColorIndexes(Color[] availableColors, CharSequence sequence) {
    val colorIndexes = new ArrayList<Integer>();
    for (int i = 0; i < 3; i++) {
      val index = indexOf(sequence, colorIndexes, availableColors, i);
      if (isDuplicatedIn(index, List.of(0, 4), colorIndexes)
          || isDuplicatedIn(index, List.of(2, 3), colorIndexes)) {
        colorIndexes.add(1);
      } else {
        colorIndexes.add(index);
      }
    }
    return colorIndexes;
  }

  boolean isDuplicatedIn(Integer index, List<Integer> values, List<Integer> colorIndexes) {
    if (values.contains(index)) {
      for (int i = 0; i < values.size(); i++) {
        if (colorIndexes.contains(values.get(i))) {
          return true;
        }
      }
    }
    return false;
  }

  private float computeHue(CharSequence sequence) {
    //    val subseq = sequence.subSequence(0, 7).toString();
    val subseq = sequence.toString().substring(sequence.length() - 7);
    return ((float) Integer.parseInt(subseq, 16)) / 0xfffffff;
  }

  private int hex(char charAt) {
    // can make this better by just converting the base
    return Integer.valueOf(String.valueOf(charAt), 16);
  }
}
