package io.sunshower.lang.primitives;

import java.util.Arrays;
import lombok.val;

/**
 * Created by haswell on 4/29/16.
 */
public class Longs {

  static final long[] FIB_ROOT = new long[]{1, 1, 1, 0};

  public static byte[] toByteArray(long[] longs) {
    final int length = longs.length;
    final byte[] bytes = new byte[length * 8];
    for (int i = 0, k = 0; i < length; i++) {
      long j = longs[i];
      bytes[k] = (byte) (j >>> 56);
      bytes[k + 1] = (byte) (j >>> 48);
      bytes[k + 2] = (byte) (j >>> 40);
      bytes[k + 3] = (byte) (j >>> 32);
      bytes[k + 4] = (byte) (j >>> 24);
      bytes[k + 5] = (byte) (j >>> 16);
      bytes[k + 6] = (byte) (j >>> 8);
      bytes[k + 7] = (byte) (j);
      k += 8;
    }
    return bytes;
  }

  public static long[] fromByteArray(byte[] longs) {
    final int len = longs.length;
    if (len % 8 != 0) {
      throw new IllegalArgumentException("Byte array must be divisible by 4");
    }
    final long[] result = new long[len / 8];
    for (int i = 0, j = 0; i < result.length; i++) {
      long r =
          (long) longs[j] << 56L
          | (long) (longs[j + 1] & 0xFF) << 48
          | (long) (longs[j + 2] & 0xFF) << 40
          | (long) (longs[j + 3] & 0xFF) << 32
          | (long) (longs[j + 4] & 0xFF) << 24
          | (long) (longs[j + 5] & 0xFF) << 16
          | (long) (longs[j + 6] & 0xFF) << 8
          | (long) (longs[j + 7] & 0xFF);
      result[i] = r;
      j += 8;
    }
    return result;
  }

  /**
   * @param l a positive long such that L < Long.MAX_VALUE (2^64 -1)
   * @return
   */
  public static long[] computeFibonacciUntil(long l) {
    assert l >= 0;

    var a = Arrays.copyOf(FIB_ROOT, FIB_ROOT.length);
    var b = Arrays.copyOf(a, a.length);
    long b11 = 1L;
    int i = 0;

    long[] result = new long[10];

    for (; b11 < l && b11 >= 0; ) {
      b = multiply(a, b);
      b11 = b[1];
      result = append(result, b, i);
      i++;
    }
    return trimLast(result);
  }

  static long[] multiply(long[] a, long[] b) {
    val a11 = a[0];
    val a12 = a[1];
    val a21 = a[2];
    val a22 = a[3];
    val b11 = b[0];
    val b12 = b[1];
    val b21 = b[2];
    val b22 = b[3];
    return new long[]{
        a11 * b11 + a12 * b12,
        a11 * b12 + a12 * b22,
        a21 * b11 + a22 * b21,
        a21 * b12 + a22 * b22
    };
  }

  private static long[] trimLast(long[] result) {
    if (result[result.length - 1] > 0) {
      return result;
    }
    int i = 1;
    while (result[result.length - i] <= 0) {
      i++;
    }
    val r = new long[result.length - i];
    System.arraycopy(result, 0, r, 0, result.length - i);
    return r;
  }

  private static long[] append(long[] result, long[] b, int i) {
    if (i + 2 < result.length) {
      result[i] = b[3];
      result[i + 1] = b[1];
      result[i + 2] = b[0];
      return result;
    } else {
      val newresult = new long[(int) (result.length * 1.5)];
      System.arraycopy(result, 0, newresult, 0, result.length);
      newresult[i] = b[3];
      newresult[i + 1] = b[1];
      newresult[i + 2] = b[0];
      return newresult;
    }
  }
}
