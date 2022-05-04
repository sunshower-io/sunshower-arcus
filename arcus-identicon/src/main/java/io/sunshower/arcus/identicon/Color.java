package io.sunshower.arcus.identicon;

import lombok.Data;

@Data
public class Color {

  private final int red;
  private final int blue;
  private final int green;

  public Color(int red, int green, int blue) {
    this.red = red;
    this.blue = blue;
    this.green = green;
  }

  public String toHexadecimal() {
    int a = ((red << 16) + (green << 8) + blue);
    return String.format("#%X", a);
  }
}
