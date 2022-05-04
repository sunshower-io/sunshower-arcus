package io.sunshower.arcus.identicon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import lombok.val;
import org.junit.jupiter.api.Test;

class ColorTest {

  @Test
  void ensureSimpleTestToHexWorks() {

    val color = new Color(255, 255, 255);
    assertEquals("#FFFFFF", color.toHexadecimal());
  }

  @Test
  void ensureComplexToHexWorks() {
    val color = new Color(34, 190, 205);
    assertEquals("#22BECD", color.toHexadecimal());
  }

  @Test
  void ensureFromHexadecimalWorks() {
    assertEquals(new Color(34, 190, 205), Colors.fromHexadecimal("#22BECD"));
  }

  @Test
  void testHslToRGb() {
    val hsl = Colors.hueSaturationLightness(.18f, .40f, .49f);
    assertEquals("#A7AF4B", hsl.toHexadecimal());
  }
}
