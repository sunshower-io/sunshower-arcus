package io.sunshower.arcus.config.condensation;

import io.sunshower.arcus.condensation.Condensation;
import io.sunshower.arcus.config.Configuration;
import io.sunshower.arcus.config.ConfigurationManager;
import java.io.File;
import java.io.InputStream;
import javax.annotation.WillNotClose;
import javax.annotation.concurrent.ThreadSafe;
import lombok.NonNull;
import lombok.val;

@ThreadSafe
public class ArcusConfigurationManager implements ConfigurationManager {


  private final File file;
  private final Condensation condensation;

  public ArcusConfigurationManager(@NonNull Condensation condensation, @NonNull File file) {
    this.file = file;
    this.condensation = condensation;
  }


  @Override
  public <T> Configuration<T> read(@NonNull Class<T> type,
      @NonNull @WillNotClose InputStream source, @NonNull String name) {
    val configObject = condensation.read(type, source);
    val result = new DefaultConfiguration<T>(configObject, name);
  }
}
