package io.sunshower.arcus.identicon;

import io.sunshower.arcus.identicon.Configuration.LightnessTransformations;
import lombok.NonNull;

class MutableConfiguration implements ConfigurationBuilder {

  private float x;
  private float y;
  private int size;
  private int padding;
  private float opacity;
  private float saturation;
  private String backgroundColor;
  private LightnessTransformations transformations;

  public MutableConfiguration(
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

  public MutableConfiguration(Configuration cfg) {
    this(
        cfg.getX(),
        cfg.getY(),
        cfg.getSize(),
        cfg.getOpacity(),
        cfg.getSaturation(),
        cfg.getPadding(),
        cfg.getBackgroundColor(),
        cfg.getTransformations());
  }

  public Configuration build() {
    return new Configuration(
        x, y, size, opacity, saturation, padding, backgroundColor, transformations);
  }

  public ConfigurationBuilder withX(float x) {
    this.x = x;
    return this;
  }

  @Override
  public ConfigurationBuilder withY(float y) {
    this.y = y;
    return this;
  }

  @Override
  public ConfigurationBuilder withSize(int size) {
    this.size = size;
    return this;
  }

  @Override
  public ConfigurationBuilder withOpacity(float opacity) {
    this.opacity = opacity;
    return this;
  }

  @Override
  public ConfigurationBuilder withSaturation(float saturation) {
    this.saturation = saturation;
    return this;
  }

  @Override
  public ConfigurationBuilder withBackgroundColor(@NonNull String s) {
    this.backgroundColor = s;
    return this;
  }
}
