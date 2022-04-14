package io.sunshower.lang.primitives;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import javax.annotation.Nullable;
import lombok.val;

/** Created by haswell on 5/26/16. */
public class Strings {

  static final int ALPHABET_SIZE = 1 << 16;

  private Strings() {}

  public static boolean isBlank(@Nullable String value) {
    if (value == null) {
      return true;
    }
    return value.trim().isEmpty();
  }

  public static byte[] getBytes(char[] chars, Charset charset) {
    val cbuffer = CharBuffer.wrap(chars);
    val bbuffer = charset.encode(cbuffer);
    return bbuffer.array();
  }

  public static int indexOf(char[] string, CharSequence pattern, int fromindex) {
    val len = pattern.length();
    if (len == 0) {
      return -1;
    }
    if (len == 1) {
      char search = pattern.charAt(0);
      for (int i = fromindex; i < string.length; i++) {
        if (string[i] == search) {
          return i;
        }
      }
    }

    val badCharShift = new int[ALPHABET_SIZE];
    Arrays.fill(badCharShift, len);

    for (int i = 0; i < len; i++) {
      val c = pattern.charAt(i);
      val l = c & 0xFF;
      badCharShift[l] = Math.min(len - i - 1, badCharShift[l]);
    }
    val slen = string.length;
    for (int i = fromindex + len + 1; i < slen; ) {
      int x = i, y = len - 1;
      while (true) {
        if (pattern.charAt(y) != string[x]) {
          i += badCharShift[(string[x] & 0xFF)];
          break;
        }
        if (y == 0) {
          return x;
        }
        x = x - 1;
        y = y - 1;
      }
    }
    return -1;
  }

  public static int indexOf(CharSequence string, CharSequence pattern) {
    if (pattern.length() == 0) {
      return 0;
    }
    int l = pattern.length();
    val charTable = characterTable(pattern);
    val offsetTable = makeOffsetTable(pattern);
    for (int i = l - 1, j; i < string.length(); ) {
      for (j = l - 1; pattern.charAt(j) == string.charAt(i); --i, --j) {
        if (j == 0) {
          return i;
        }
      }
      i += Math.max(offsetTable[l - 1 - j], charTable[string.charAt(i)]);
    }
    return -1;
  }

  public static int indexOf(char[] string, char[] pattern) {
    if (pattern.length == 0) {
      return 0;
    }
    int l = pattern.length;
    val charTable = characterTable(pattern);
    val offsetTable = makeOffsetTable(pattern);
    for (int i = l - 1, j; i < string.length; ) {
      for (j = l - 1; pattern[j] == string[i]; --i, --j) {
        if (j == 0) {
          return i;
        }
      }
      i += Math.max(offsetTable[l - 1 - j], charTable[string[i]]);
    }
    return -1;
  }

  private static int[] characterTable(CharSequence search) {
    final int ALPHABET_SIZE = 256; // 65536
    val table = new int[ALPHABET_SIZE];
    for (int i = 0, slen = search.length(), tlen = table.length; i < tlen; ++i) {
      table[i] = slen;
    }
    for (int i = 0, slen = search.length(); i < slen; ++i) {
      table[search.charAt(i)] = slen - 1 - i;
    }
    return table;
  }

  private static int[] characterTable(char[] search) {
    val table = new int[ALPHABET_SIZE];
    for (int i = 0, slen = search.length, tlen = table.length; i < tlen; ++i) {
      table[i] = slen;
    }
    for (int i = 0, slen = search.length; i < slen; ++i) {
      table[search[i]] = slen - 1 - i;
    }
    return table;
  }

  private static int[] makeOffsetTable(CharSequence needle) {
    int len = needle.length();
    int[] table = new int[len];
    int lastPrefixPosition = len;
    for (int i = len; i > 0; --i) {
      if (isPrefix(needle, i)) {
        lastPrefixPosition = i;
      }
      table[needle.length() - i] = lastPrefixPosition - i + needle.length();
    }
    for (int i = 0; i < len - 1; ++i) {
      int slen = suffixLength(needle, i);
      table[slen] = len - 1 - i + slen;
    }
    return table;
  }

  private static int[] makeOffsetTable(char[] search) {
    int[] table = new int[search.length];
    int lastPrefixPosition = search.length;
    for (int i = search.length; i > 0; --i) {
      if (isPrefix(search, i)) {
        lastPrefixPosition = i;
      }
      table[search.length - i] = lastPrefixPosition - i + search.length;
    }
    for (int i = 0; i < search.length - 1; ++i) {
      int slen = suffixLength(search, i);
      table[slen] = search.length - 1 - i + slen;
    }
    return table;
  }

  private static boolean isPrefix(char[] search, int p) {
    for (int i = p, j = 0, slen = search.length; i < slen; ++i, ++j) {
      if (search[i] != search[j]) {
        return false;
      }
    }
    return true;
  }

  private static int suffixLength(char[] search, int p) {
    int len = 0;
    for (int i = p, slen = search.length, j = slen - 1;
        i >= 0 && search[i] == search[j];
        --i, --j) {
      len += 1;
    }
    return len;
  }

  private static boolean isPrefix(CharSequence search, int p) {
    for (int i = p, j = 0, slen = search.length(); i < slen; ++i, ++j) {
      if (search.charAt(i) != search.charAt(j)) {
        return false;
      }
    }
    return true;
  }

  private static int suffixLength(CharSequence search, int p) {
    int len = 0;
    for (int i = p, slen = search.length(), j = slen - 1;
        i >= 0 && search.charAt(i) == search.charAt(j);
        --i, --j) {
      len += 1;
    }
    return len;
  }
}
