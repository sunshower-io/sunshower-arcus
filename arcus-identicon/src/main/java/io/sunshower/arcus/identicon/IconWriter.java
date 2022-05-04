package io.sunshower.arcus.identicon;


public interface IconWriter {

  void setBackground(Color fillColor);

  void setBackground(Color fillColor, float opacity);


  void addPath(Path path);

  default int getSize() {
    return 0;
  }





}
