package com.aire.ux.test;

import static com.aire.ux.test.Nodes.node;
import static java.lang.String.format;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import io.sunshower.arcus.ast.Symbol;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import lombok.val;

@SuppressFBWarnings
public class Node {

  static final String EMPTY_CONTENT = "".intern();
  private final String type;
  private final Node parent;

  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  private final String content;

  @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
  private final List<Node> children;

  private final BitSet states;
  private final Map<String, String> attributes;

  public Node(String type) {
    this(type, EMPTY_CONTENT);
  }

  public Node(String type, String content) {
    this(null, type, content);
  }

  public Node(Node parent, String type, String content) {
    this(type, content, parent, new ArrayList<>(), new LinkedHashMap<>());
  }

  Node(String type, String content, List<Node> children, Map<String, String> attributes) {
    this(type, content, null, children, attributes);
  }

  Node(
      String type,
      String content,
      Node parent,
      List<Node> children,
      Map<String, String> attributes) {
    this.type = type;
    this.parent = parent;
    this.content = content;
    this.children = children;
    this.attributes = attributes;
    this.states = new BitSet();
  }

  Node(Node parent, Node copy) {
    this(copy.type, copy.content, parent, copy.children, copy.attributes);
  }

  public static NodeAdapter<Node> getAdapter() {
    return new NodeNodeAdapter();
  }

  public Node setContent(String content) {
    return new Node(type, content, parent, children, attributes);
  }

  public void addChildren(Collection<? extends Node> children) {
    this.children.addAll(
        children.stream().map(t -> new Node(this, t)).collect(Collectors.toList()));
  }

  public void addChild(Node child) {
    this.children.add(new Node(this, child));
  }

  public List<Node> setChildren(Collection<? extends Node> children) {
    val result = new ArrayList<>(this.children);
    this.children.clear();
    this.children.addAll(
        children.stream().map(t -> new Node(this, t)).collect(Collectors.toList()));
    return result;
  }

  public String setAttribute(String attribute, String value) {
    return attributes.put(attribute, value);
  }

  public boolean hasAttribute(String attribute) {
    return attributes.containsKey(attribute);
  }

  @SuppressFBWarnings
  public Node children(String... children) {
    for (val child : children) {
      node(child);
    }
    return this;
  }

  Node state(NodeAdapter.State state) {
    setState(state);
    return this;
  }

  /** Builder methods */
  public Node children(Node... children) {
    setChildren(List.of(children));
    return this;
  }

  public void setState(NodeAdapter.State state) {
    this.states.set(state.ordinal());
  }

  public boolean hasState(NodeAdapter.State state) {
    return this.states.get(state.ordinal());
  }

  public String id() {
    return getAttribute("id");
  }

  public Node id(String id) {
    return attribute("id", id);
  }

  public Node attribute(String key, String value) {
    setAttribute(key, value);
    return this;
  }

  public Node child(Node child) {
    return children(child);
  }

  public Node child(String child) {
    return child(node(child));
  }

  public Node content(String content) {
    return setContent(content);
  }

  public String content() {
    return content;
  }

  @Override
  public String toString() {
    val result = new StringBuilder();
    int depth = 0;
    writeNode(this, result, depth);
    return result.toString();
  }

  boolean hasContent() {
    return !EMPTY_CONTENT.equals(content);
  }

  private void writeNode(Node node, StringBuilder result, int depth) {
    val indent = " ".repeat(depth);
    result.append(indent).append("<").append(node.type);
    if (!node.attributes.isEmpty()) {
      for (val kv : node.attributes.entrySet()) {
        result.append(" ").append(kv.getKey()).append("=\"").append(kv.getValue()).append("\"");
      }
    }
    result.append(">").append("\n");
    if (node.hasContent()) {
      val content = node.content.split("\\R");
      val ind = indent + " ";
      for (val c : content) {
        result.append(ind).append(c).append("\n");
      }
    }

    for (val child : node.children) {
      writeNode(child, result, depth + 1);
    }
    result.append(indent).append(format("</%s>", node.type)).append("\n");
  }

  public String getAttribute(String key) {
    return attributes.get(key);
  }

  public String getType() {
    return type;
  }

  public List<Node> getChildren() {
    return Collections.unmodifiableList(children);
  }

  public Node getParent() {
    return parent;
  }

  public static enum DomStates implements NodeAdapter.State {
    /** dom state active */
    Active(":active"),
    /** dom state focused */
    Focused(":focus"),

    /** dom state focus-within */
    FocusWithin(":focus-within"),
    /** dom state target */
    Target(":target"),
    /** dom state hover */
    Hover(":hover"),
    /** dom state visited */
    Visited(":visited"),
    /** dom state focus visible */
    FocusVisible(":focus-visible"),

    /** has no children */
    Empty(":empty"),

    Checked(":checked"),

    Default(":default"),

    Disabled(":disabled"),

    Enabled(":enabled"),

    FullScreen(":full-screen"),
    InRange(":in-range"),
    Indeterminate(":indeterminate"),
    Invalid(":invalid"),

    Link(":link"),
    ReadOnly(":read-only"),
    Required(":required");

    final String value;

    DomStates(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }

    public Symbol toSymbol() {
      return Symbol.symbol(value.substring(1));
    }
  }

  private static final class NodeNodeAdapter implements NodeAdapter<Node> {

    final Map<String, DomStates> nameMap;

    NodeNodeAdapter() {
      nameMap = new HashMap<>();
      for (val n : DomStates.values()) {
        nameMap.put(n.value.substring(1), n);
      }
    }

    @Override
    public List<Node> getChildren(Node current) {
      return current.children;
    }

    @Nullable
    @Override
    public Node getParent(@Nonnull Node current) {
      return current.parent;
    }

    @Override
    public Node setChildren(Node current, Collection<? extends Node> children) {
      current.setChildren(children);
      return current;
    }

    @Override
    public String getAttribute(Node current, String key) {
      return current.getAttribute(key);
    }

    @Override
    public Node setAttribute(Node node, String key, String value) {
      node.setAttribute(key, value);
      return node;
    }

    @Override
    public boolean hasAttribute(Node c, String key) {
      return c.attributes.containsKey(key);
    }

    @Override
    public Node clone(Node value) {
      return new Node(value.type, value.content, new ArrayList<>(), value.attributes);
    }

    @Override
    public String getType(Node n) {
      return n.getType();
    }

    @Override
    public void setState(@Nonnull Node element, @Nonnull State state) {
      element.setState(state);
    }

    @Override
    public boolean hasState(@Nonnull Node element, @Nonnull State state) {
      return element.hasState(state);
    }

    @Nullable
    @Override
    public Node getSucceedingSibling(@Nonnull Node element) {
      val parent = element.parent;
      if (parent != null) {
        val children = parent.children;
        val idx = children.indexOf(element);
        if (idx >= 0 && (idx + 1) < children.size()) {
          return children.get(idx + 1);
        }
      }
      return null;
    }

    @Override
    public State stateFor(String name) {
      val r = nameMap.get(name);
      if (r == null) {
        throw new NoSuchElementException("No state with name: " + name);
      }
      return r;
    }
  }
}
