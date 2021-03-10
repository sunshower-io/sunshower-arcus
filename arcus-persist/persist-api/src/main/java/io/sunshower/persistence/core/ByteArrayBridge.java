package io.sunshower.persistence.core;

import io.sunshower.common.Identifier;
import io.sunshower.encodings.Base58;
import io.sunshower.encodings.Encoding;
import org.hibernate.search.bridge.TwoWayStringBridge;

public class ByteArrayBridge implements TwoWayStringBridge {
  static final Encoding encoding = Base58.getInstance(Base58.Alphabets.Default);

  @Override
  public String objectToString(Object object) {
    if (object != null) {
      if (object instanceof String) {
        return (String) object;
      }
      if (object instanceof byte[]) {
        return encoding.encode((byte[]) object);
      }
    }
    return "null";
  }

  @Override
  public Object stringToObject(String stringValue) {
    if (stringValue != null) {
      return Identifier.valueOf(stringValue);
    }
    return "null";
  }
}
