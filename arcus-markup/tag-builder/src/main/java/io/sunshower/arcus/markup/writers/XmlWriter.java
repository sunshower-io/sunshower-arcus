package io.sunshower.arcus.markup.writers;

import io.sunshower.arcus.markup.Tag;
import io.sunshower.arcus.markup.TagWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Map;
import java.util.regex.Pattern;
import lombok.val;

public class XmlWriter implements TagWriter {


  private static final Pattern pattern = Pattern.compile("\n");
  private final PrintWriter writer;


  public XmlWriter(OutputStream outputStream) {
    this.writer = new PrintWriter(new OutputStreamWriter(outputStream));
  }

  @Override
  public void writeContent(Tag tag, int depth) {
    val content = tag.content();
    if (content != null) {
      val results = pattern.split(content);
      for (val result : results) {
        indent(depth + 1);
        writer.write(result);
        writer.append("\n");
      }
    }

  }

  @Override
  public void openTag(Tag tag, int depth) {
    indent(depth);
    writer.write("<");
    writer.write(tag.name());
    writer.write("\n");

  }


  @Override
  public void writeAttributes(Map<CharSequence, Serializable> attributes, int i) {
    for (val entry : attributes.entrySet()) {
      indent(i + 1);
      writer.write(String.valueOf(entry.getKey()));
      writer.write("=\"");
      writer.write(String.valueOf(entry.getValue()));
      writer.write("\"");
      writer.write("\n");
    }
  }

  @Override
  public void closeTag(Tag tag, int depth) {
    indent(depth);
    writer.write("/>\n");
    writer.flush();
  }

  private void indent(int depth) {
    for (int i = 0; i < depth; i++) {
      writer.write(" ");
    }
  }
}
