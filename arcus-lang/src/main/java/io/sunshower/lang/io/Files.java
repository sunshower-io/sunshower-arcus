package io.sunshower.lang.io;

import io.sunshower.lambda.Option;
import java.io.File;

public class Files {

  public static Option<String> getExtension(File file) {
    return Option.of(file).fmap(File::getAbsolutePath).flatMap(Files::getExtension);
  }

  public static Option<String> getExtension(String file) {
    return Option.of(file)
        .filter(f -> f.contains("."))
        .fmap(t -> t.substring(t.lastIndexOf('.') + 1));
  }
}
