package io.sunshower.arcus.identicon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import javax.annotation.WillNotClose;
import lombok.val;

public class Jdenticon {

  static final Predicate<String> HASH_PATTERN =
      Pattern.compile("^[a-fA-F0-9]{40}$").asMatchPredicate();

  public static void toSvg(Object o, Configuration cfg, @WillNotClose OutputStream outputStream) {
    if (o instanceof String && isHash((String) o)) {
      Identicon.writeHashToSvg((String) o, cfg, outputStream);
    } else {
      Identicon.toSvg(o, cfg, outputStream);
    }
  }

  private static boolean isHash(String o) {
    return HASH_PATTERN.test(o);
  }

  public static String toSvg(Object o, Configuration configuration) {
    try (val output = new ByteArrayOutputStream()) {
      toSvg(o, configuration, output);
      return output.toString(StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static String toSvg(Object o) {
    return toSvg(o, Configuration.getDefault());
  }
}
