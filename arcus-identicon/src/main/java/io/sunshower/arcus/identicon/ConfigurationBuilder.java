package io.sunshower.arcus.identicon;

public interface ConfigurationBuilder {

  ConfigurationBuilder withX(float x);

  ConfigurationBuilder withY(float y);

  ConfigurationBuilder withSize(int size);

  ConfigurationBuilder withOpacity(float opacity);

  ConfigurationBuilder withSaturation(float saturation);

  ConfigurationBuilder withBackgroundColor(String s);

  Configuration build();

  ConfigurationBuilder withPadding(int i);
}
