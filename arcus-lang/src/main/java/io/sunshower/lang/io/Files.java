package io.sunshower.lang.io;

import java.io.File;
import java.util.Optional;

public class Files {

  public static Optional<String> getExtension(File file) {
    return Optional.ofNullable(file).map(File::getAbsolutePath).flatMap(Files::getExtension);
  }

  public static Optional<String> getExtension(String file) {
    return Optional.ofNullable(file)
        .filter(f -> f.contains("."))
        .map(t -> t.substring(t.lastIndexOf('.') + 1));
  }
}
