package io.sunshower.lang;

import lombok.val;

public final class OperatingSystems {

  private static Type type;

  static {
    type = getCurrent();
  }

  public static Type getCurrent() {
    if (type == null) {
      val systemString = Environment.getDefault().getSystemProperty("os.name");
      if (systemString.contains("win")) {
        type = Type.Windows;
      } else if (systemString.contains("nix")
          || systemString.contains("nux")
          || systemString.contains("aix")) {
        type = Type.Linux;
      } else if (systemString.contains("mac")) {
        type = Type.Mac;
      } else if (systemString.contains("sunos")) {
        type = Type.Solaris;
      }
    }
    return type;
  }

  public enum Type {
    Windows,
    Linux,
    Mac,
    Solaris
  }
}
