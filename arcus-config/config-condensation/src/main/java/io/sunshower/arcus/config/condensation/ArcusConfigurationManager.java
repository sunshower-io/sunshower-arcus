package io.sunshower.arcus.config.condensation;

import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.arcus.config.Configuration;
import io.sunshower.arcus.config.ConfigurationManager;
import java.io.File;
import java.io.InputStream;
import lombok.NonNull;

public class ArcusConfigurationManager implements ConfigurationManager {


  private final File file;
  private final Condensation condensation;

  public ArcusConfigurationManager(@NonNull Condensation condensation, @NonNull File file) {
    this.file = file;
    this.condensation = condensation;
  }


  @Override
  public <T> Configuration<T> read(Class<T> type, InputStream source, String name) {
    return null;
//    condensation.
  }
}
