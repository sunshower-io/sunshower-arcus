package io.sunshower.lang.primitives;

import java.io.PrintWriter;
import java.util.ArrayList;
import lombok.val;

abstract class AbstractRopeLike implements RopeLike {

  @Override
  public void writeTree(PrintWriter out) {
    writeTree(out, this, "", true);
  }

  public RopeLike append(final CharSequence sequence) {
    return Ropes.append(this, new RopeLikeOverCharSequence(sequence));
  }

  @Override
  public int hashCode() {
    int h = 0;
    int len = length();
    for (int i = 0; i < len; i++) {
      h = 31 * h + charAt(i);
    }
    return h;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) {
      return false;
    }
    if (o == this) {
      return true;
    }

    if (o instanceof CharSequence seq) {
      if (length() != seq.length()) {
        return false;
      }
      for (int i = 0; i < length(); i++) {
        if (charAt(i) != seq.charAt(i)) {
          return false;
        }
      }
      return true;
    }
    return false;
  }

  private void writeTree(PrintWriter out, RopeLike node, String indent, boolean last) {
    if (node.equals(this)) {
      out.append(
              "rope(%d,%d)[%s]"
                  .formatted(node.weight(), node.length(), new String(node.characters())))
          .append("\n");
    } else {
      out.append(indent)
          .append(last ? "└╴" : "├╴")
          .append(
              "rope(%d,%d)[%s]"
                  .formatted(node.weight(), node.length(), new String(node.characters())))
          .append("\n");
    }
    indent = indent + (last ? "   " : "│  ");
    val results = new ArrayList<RopeLike>();
    val left = node.getLeft();
    val right = node.getRight();
    if (left != null) {
      results.add(left);
    }
    if (right != null) {
      results.add(right);
    }

    val iter = results.iterator();
    while (iter.hasNext()) {
      val child = iter.next();
      val isLast = !iter.hasNext();
      writeTree(out, child, indent, isLast);
    }
  }

  @SuppressWarnings("PMD")
  public abstract AbstractRopeLike clone();
}
