package io.sunshower.arcus.identicon;

import static io.sunshower.arcus.identicon.Colors.hueSaturationLightness;

import java.util.function.Function;
import lombok.Getter;
import lombok.val;

@Getter
public final class Configuration {

  static final int DEFAULT_SIZE = 100;
  static final int DEFAULT_PADDING = 0;
  private static final float DEFAULT_OPACITY = 1f;

  private final float x;
  private final float y;
  private final int size;
  private final int padding;
  private final float opacity;
  private final float saturation;
  private final String backgroundColor;
  private final LightnessTransformations transformations;

  public Configuration(
      float x,
      float y,
      int size,
      float opacity,
      float saturation,
      int padding,
      String backgroundColor,
      LightnessTransformations transformations) {
    this.x = x;
    this.y = y;
    this.size = size;
    this.opacity = opacity;
    this.padding = padding;
    this.saturation = saturation;
    this.backgroundColor = backgroundColor;
    this.transformations = transformations;
  }

  public static Configuration getConfiguration(int size, int padding) {
    return getConfiguration(size, padding, DEFAULT_OPACITY);
  }

  public static Configuration getConfiguration(int size, int padding, float opacity) {
    return new Configuration(
        0f,
        0f,
        size,
        opacity,
        0.5F,
        padding,
        "#FFFFFF",
        new LightnessTransformations(createLightness(0.4F, 0.8F), createLightness(0.3f, 0.9f)));

  }


  public static Configuration getDefault() {
    return getConfiguration(DEFAULT_SIZE, DEFAULT_PADDING);
  }

  static Function<Float, Float> createLightness(float min, float max) {
    return value -> {
      val i = min + value * (max - min);
      return i < 0 ? 0 : i > 1 ? 1 : i;
    };
  }

  Color[] colors(float hue) {
    return new Color[]{
        hueSaturationLightness(0f, 0f, transformations.grayscale.apply(0f)),
        hueSaturationLightness(hue, saturation, transformations.color.apply(0.5F), true),
        hueSaturationLightness(0f, 0f, transformations.grayscale.apply(1f)),
        hueSaturationLightness(hue, saturation, transformations.color.apply(1F), true),
        hueSaturationLightness(hue, saturation, transformations.color.apply(0F), true)
    };
  }

  public static class LightnessTransformations {

    final Function<Float, Float> color;
    final Function<Float, Float> grayscale;

    public LightnessTransformations(
        Function<Float, Float> color,
        Function<Float, Float> grayscale) {
      this.color = color;
      this.grayscale = grayscale;
    }
  }
}
