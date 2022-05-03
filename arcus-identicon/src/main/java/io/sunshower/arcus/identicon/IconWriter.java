package io.sunshower.arcus.identicon;

import io.sunshower.arcus.markup.Tag;

public interface IconWriter {

  void setBackground(Color fillColor);

  void setBackground(Color fillColor, float opacity);

  default int getSize() {
    return 0;
  }





}
