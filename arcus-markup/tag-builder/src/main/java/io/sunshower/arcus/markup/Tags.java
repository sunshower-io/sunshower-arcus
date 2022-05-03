package io.sunshower.arcus.markup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.val;

public class Tags {


  public static Tag root(String type) {
    return new DefaultTag(type);
  }

  public static Tag tag(String type) {
    return new DefaultTag(type);
  }


}

final class DefaultTag implements Tag {

  private final String name;
  private final Tag parent;
  private final Collection<Tag> children;
  private final LinkedHashMap<CharSequence, Serializable> attributes;
  private CharSequence content;

  DefaultTag(String name) {
    this(name, null, Collections.emptyList());
  }

  DefaultTag(String name, Tag parent) {
    this(name, parent, Collections.emptyList());
  }

  DefaultTag(String name, Tag parent, Collection<Tag> children) {
    this(name, parent, children, Collections.emptyMap());
  }

  DefaultTag(@NonNull String name,
      @Nullable Tag parent,
      @NonNull Collection<Tag> children,
      @NonNull Map<String, Serializable> attributes) {
    this.name = name;
    this.parent = parent;
    this.children = new ArrayList<>(children);
    this.attributes = new LinkedHashMap<>(attributes);
  }

  @Override
  public String name() {
    return name;
  }

  @Nullable
  @Override
  public Tag parent() {
    return parent;
  }

  @Override
  public Tag content(CharSequence content) {
    this.content = content;
    return this;
  }

  @Nullable
  @Override
  public CharSequence content() {
    return content;
  }

  @Override
  public @NonNull Collection<Tag> children() {
    return children;
  }

  @Override
  public @NonNull Tag child(@NonNull Tag child) {
    children.add(child);
    return this;
  }

  @Override
  public @NonNull Tag child(@NonNull Supplier<Tag> child) {
    children.add(child.get());
    return this;
  }

  @Override
  public @NonNull Tag attribute(@NonNull CharSequence key) {
    attributes.put(key, null);
    return this;
  }

  @Override
  public @NonNull Tag attribute(CharSequence key, Serializable value) {
    attributes.put(key, value);
    return this;
  }

  @Override
  public void write(TagWriter writer) {
    doWrite(writer, this, 0);
  }

  @Override
  public Map<CharSequence, Serializable> attributes() {
    return Collections.unmodifiableMap(attributes);
  }

  private void doWrite(TagWriter writer, Tag tag, int i) {
    writer.openTag(tag, i);
    writer.writeAttributes(tag.attributes(), i);
    writer.writeContent(tag, i);
    for (val child : tag.children()) {
      doWrite(writer, child, i + 1);
    }
    writer.closeTag(tag, i);
  }
}
