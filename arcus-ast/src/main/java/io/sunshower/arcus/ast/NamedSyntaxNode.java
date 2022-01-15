package io.sunshower.arcus.ast;

import static java.lang.String.format;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.val;

public class NamedSyntaxNode<T, U> extends AbstractSyntaxNode<T, U> {

  @Getter final String name;

  public NamedSyntaxNode(
      String name,
      Symbol symbol,
      U source,
      T value,
      String content,
      List<SyntaxNode<T, U>> children,
      Map<String, String> properties) {
    super(symbol, source, content, value, children, properties);
    this.name = name;
  }

  public NamedSyntaxNode(String name, Symbol symbol, U source, T value, String content) {
    super(symbol, source, content, value);
    this.name = name;
  }

  public NamedSyntaxNode(String name, Symbol symbol, U source, T value) {
    this(name, symbol, source, value, null);
  }

  public String toString() {
    val content = getContent();

    return format(
        "%s[symbol:%s, name: %s]{%s}",
        getClass().getSimpleName(),
        symbol,
        name,
        content == null ? null : content.replaceAll("\\n", " "));
  }

  @Override
  @SuppressFBWarnings
  @SuppressWarnings("PMD")
  public SyntaxNode<T, U> clone() {
    return new NamedSyntaxNode<>(
        name, symbol, source, value, getContent(), new ArrayList<>(), properties);
  }
}
