package io.sunshower.lang.primitives;

import lombok.NonNull;
import lombok.val;

public class Arrays {

  public static char[] remove(@NonNull char[] es, int start, int toRemove) {
    val len = es.length;
    val newResult = new char[len - toRemove];
    System.arraycopy(es, 0, newResult, 0, start);
    System.arraycopy(es, start + toRemove, newResult, start, len - (start + toRemove));
    return newResult;
  }
}
