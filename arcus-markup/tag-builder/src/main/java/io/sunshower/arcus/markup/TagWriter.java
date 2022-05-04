package io.sunshower.arcus.markup;

import java.io.Serializable;
import java.util.Map;

public interface TagWriter {

  void writeContent(Tag tag, int depth);

  void openTag(Tag tag, int depth);

  void writeAttributes(Map<CharSequence, Serializable> attributes, int i);

  void closeTag(Tag tag, int depth);
}
