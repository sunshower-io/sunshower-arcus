package io.sunshower.arcus.identicon.renderers.svg;

import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.lang.primitives.Rope;
import lombok.val;
import org.junit.jupiter.api.Test;

class SVGRendererTest {

  @Test
  void ensureMatcherWorksForRGB() {
    val matcher = SVGRenderer.RGBA_PATTERN.matcher(new Rope("#FFFFFF"));
    assertTrue(matcher.matches());
    assertEquals(matcher.group(0), "#FFFFFF");
  }
}
