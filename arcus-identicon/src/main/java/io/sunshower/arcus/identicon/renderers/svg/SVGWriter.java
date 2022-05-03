package io.sunshower.arcus.identicon.renderers.svg;

import static io.sunshower.arcus.markup.Tags.tag;
import static java.lang.String.format;

import io.sunshower.arcus.identicon.Color;
import io.sunshower.arcus.identicon.IconWriter;
import io.sunshower.arcus.markup.Tag;
import io.sunshower.arcus.markup.Tags;

public class SVGWriter implements IconWriter {

  private final Tag tag;
  private final int size;

  public SVGWriter(final int size) {
    this.size = size;
    this.tag = Tags.root("svg")
        .attribute("xmlns", "http://www.w3.org/2000/svg")
        .attribute("width", size)
        .attribute("height", size)
        .attribute("viewBox", format("0 0 %d %d", size, size))
        .attribute("preserveAspectRatio", "xMidYMid meet");
  }

  @Override
  public void setBackground(Color fillColor) {
    tag.child(
        tag("rect")
            .attribute("width", "100%")
            .attribute("height", "100%")
            .attribute("fill", fillColor.toHexadecimal())
    );
  }

  @Override
  public void setBackground(Color fillColor, float opacity) {
    tag.child(
        tag("rect")
            .attribute("width", "100%")
            .attribute("height", "100%")
            .attribute("fill", fillColor.toHexadecimal())
            .attribute("opacity", String.format("%2f", opacity))
    );
  }


}
