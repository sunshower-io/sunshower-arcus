package io.sunshower.arcus.identicon.renderers.svg;

import static org.junit.jupiter.api.Assertions.*;

import io.sunshower.arcus.identicon.Point;
import lombok.val;
import org.junit.jupiter.api.Test;

class SvgPathTest {


  @Test
  void ensureBuildingPathWorks() {
    val c = new Point(20, 20);
    val r = 40;
    val p = new SvgPath().addCircle(c, r).getPath();
    assertEquals("M20 40a20,20 0 1,1 40,0a20,20 0 1,1 -40,0", p.toString());
  }


}