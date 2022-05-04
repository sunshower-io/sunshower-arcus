package io.sunshower.arcus.markup;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import lombok.NonNull;
import lombok.val;

public interface Tag {

  String name();

  @Nullable
  Tag parent();

  Tag content(CharSequence content);

  @Nullable
  CharSequence content();

  @NonNull
  Collection<Tag> children();

  @NonNull
  Tag child(@NonNull Tag child);

  @NonNull
  default Tag children(@NonNull Collection<Tag> children) {
    Tag result = this;
    for (val child : children) {
      result = result.child(child);
    }
    return result;
  }

  @NonNull
  default Tag children(@NonNull Tag... children) {
    Tag result = this;
    for (val child : children) {
      result = result.child(child);
    }
    return result;
  }

  @NonNull
  Tag child(@NonNull Supplier<Tag> child);

  @NonNull
  Tag attribute(@NonNull CharSequence key);

  @NonNull
  Tag attribute(CharSequence key, Serializable value);

  void write(TagWriter writer);

  Map<CharSequence, Serializable> attributes();
}
