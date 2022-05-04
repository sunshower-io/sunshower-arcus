package io.sunshower.arcus.identicon;

import io.sunshower.arcus.identicon.renderers.svg.SVGRenderer;
import io.sunshower.arcus.identicon.renderers.svg.SVGWriter;
import io.sunshower.arcus.markup.Tag;
import io.sunshower.arcus.markup.writers.XmlWriter;
import io.sunshower.lang.common.hash.Hashes;
import io.sunshower.lang.common.hash.Hashes.Algorithm;
import io.sunshower.lang.common.hash.Hashes.HashCode;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import javax.annotation.WillNotClose;
import lombok.NonNull;
import lombok.val;

public class Identicon {

  static final HashCode hashcode;


  static {
    hashcode = Hashes.hashCode(Algorithm.SHA1);
  }


  public static void create(@NonNull Object toHash, @WillNotClose Writer writer)
      throws IOException {
    val bytes = hashcode.digest(toHash);
    for (val b : bytes) {
      int d = (int) b & 0xFF;
      val s = Integer.toHexString(d);
      if (s.length() % 2 == 0) {
        writer.write("0");
        writer.write(s);
      } else {
        writer.write(s);
      }
    }
    writer.flush();
  }


  public static Tag toSvg(Object o) {
    return toSvg(o, Configuration.DEFAULT_SIZE, Configuration.DEFAULT_PADDING);
  }

  public static Tag toSvg(Object o, int size, int padding) {
    return toSvg(o, size, padding, 1);
  }

  public static Tag toSvg(Object o, int size, int padding, float opacity) {
    return toSvg(o, Configuration.getConfiguration(size, padding, opacity));
  }

  public static void toSvg(Object o, Configuration cfg, OutputStream outputStream) {
    try {
      val tag = toSvg(o, cfg);
      tag.write(new XmlWriter(outputStream));
      outputStream.flush();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

  public static Tag toSvg(Object o, Configuration configuration) {
    try (
        val baos = new ByteArrayOutputStream();
        val actualOutput = new ByteArrayOutputStream();
        val result = new PrintWriter(new OutputStreamWriter(actualOutput));
    ) {
      create(o, result);
      result.flush();
      actualOutput.flush();
      val tag = generateSvg(actualOutput.toString(StandardCharsets.UTF_8), configuration);
      tag.write(new XmlWriter(baos));
      baos.flush();
      return tag;
    } catch (IOException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private static Tag generateSvg(String toString, Configuration configuration) {
    val writer = new SVGWriter(configuration.getSize());
    val generator = new IconGenerator(new SVGRenderer(writer, configuration.getOpacity()),
        configuration);
    generator.apply(toString);
    return writer.getRoot();
  }

  public static void writeHashToSvg(String hash, Configuration cfg, OutputStream outputStream) {
    try {
      val tag = generateSvg(hash, cfg);
      tag.write(new XmlWriter(outputStream));
      outputStream.flush();
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }
}
