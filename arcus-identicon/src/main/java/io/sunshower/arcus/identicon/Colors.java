package io.sunshower.arcus.identicon;

import lombok.NonNull;
import lombok.val;

public class Colors {


  private static final float[] correctors = new float[]{
      0.55f, 0.5f, 0.5f, 0.46f, 0.6f, 0.55f, 0.55f
  };

  public static Color fromHexadecimal(@NonNull String value) {
    val normalized = value.charAt(0) == '#' ? value.substring(1) : value;
    val v = Integer.parseInt(normalized, 16);
    return new Color((v >> 16) & 255, (v >> 8) & 255, v & 255);
  }

  public static Color hueSaturationLightness(float hue, float saturation, float lightness) {
    return hueSaturationLightness(hue, saturation, lightness, false);
  }

  /**
   * create a color from hsl
   *
   * @param hue        the hue
   * @param saturation the saturation
   * @param lightness  the lightness
   * @return a color from HSL
   */
  public static Color hueSaturationLightness(float hue, float saturation, float lightness,
      boolean correct) {
    if (correct) {
      lightness = correct(hue, lightness);
    }
    float red, green, blue;
    if (saturation == 0f) {
      red = green = blue = lightness;
    } else {
      val q =
          lightness < 0.5
              ? lightness * (1 + saturation)
              : lightness + saturation - lightness * saturation;
      val p = 2 * lightness - q;
      red = hueToRgb(p, q, hue + (1f / 3f));
      green = hueToRgb(p, q, hue);
      blue = hueToRgb(p, q, hue - (1f / 3f));
    }
    return new Color(Math.round(red * 255), Math.round(green * 255), Math.round(blue * 255));
  }

  private static float hueToRgb(float p, float q, float t) {
    if (t < 0) {
      t += 1;
    }
    if (t > 1) {
      t -= 1;
    }
    if (t < (1f / 6f)) {
      return p + (q - p) * 6 * t;
    }
    if (t < (1f / 2f)) {
      return q;
    }
    if (t < (2f / 3f)) {
      return p + (q - p) * (2f / 3f - t) * 6;
    }
    return p;
  }

  private static float correct(float hue, float lightness) {
    val cfactor = correctors[(int) Math.floor(hue * 6 + 0.5)];
    return lightness < 0.5F ?
        lightness * cfactor * 2F
        : cfactor + (lightness - 0.5F) * (1F - cfactor) * 2F;
  }

}
