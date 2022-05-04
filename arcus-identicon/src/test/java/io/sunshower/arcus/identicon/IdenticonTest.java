package io.sunshower.arcus.identicon;


import static org.junit.jupiter.api.Assertions.assertEquals;

import io.sunshower.arcus.markup.writers.XmlWriter;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

class IdenticonTest {

  @Test
  void ensureOpacityWorks() {
    val result = Jdenticon.toSvg("35318264c9a98faf79965c270ac80c5606774df1", Configuration.getConfiguration(128, 0, 0.5f));
    System.out.println(result);
  }

  @Test
  void ensurePaddingWorks() {
    val result = Jdenticon.toSvg("35318264c9a98faf79965c270ac80c5606774df1", Configuration.getConfiguration(128, 2));

    assertEquals("<svg\n"
                 + " xmlns=\"http://www.w3.org/2000/svg\"\n"
                 + " width=\"128\"\n"
                 + " height=\"128\"\n"
                 + " viewBox=\"0 0 128 128\"\n"
                 + " preserveAspectRatio=\"xMidYMid meet\"\n"
                 + "> <path\n"
                 + "  fill=\"#E6E6E6\"\n"
                 + "  d=\"M144 208a-32,-32 0 1, 1 -64,0a-32,-32 0 1, 1 64,0M48 208a-32,-32 0 1, 1 -64,0a-32,-32 0 1, 1 64,0M48 -80a-32,-32 0 1, 1 -64,0a-32,-32 0 1, 1 64,0M144 -80a-32,-32 0 1, 1 -64,0a-32,-32 0 1, 1 64,0M240 112a-32,-32 0 1, 1 -64,0a-32,-32 0 1, 1 64,0M-48 112a-32,-32 0 1, 1 -64,0a-32,-32 0 1, 1 64,0M-48 16a-32,-32 0 1, 1 -64,0a-32,-32 0 1, 1 64,0M240 16a-32,-32 0 1, 1 -64,0a-32,-32 0 1, 1 64,0\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + " <path\n"
                 + "  fill=\"#33995E\"\n"
                 + "  d=\"M256 256L160 256L160 160ZM-128 256L-128 160L-32 160ZM-128 -128L-32 -128L-32 -32ZM256 -128L256 -32L160 -32Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + " <path\n"
                 + "  fill=\"#66CC91\"\n"
                 + "  d=\"M160 160L64 160L64 64L160 64ZM97 73L73 121L121 121ZM-32 160L-32 64L64 64L64 160ZM54 97L7 73L7 121ZM-32 -32L64 -32L64 64L-32 64ZM30 54L54 7L6 7ZM160 -32L160 64L64 64L64 -32ZM73 30L121 54L121 6Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + "</svg>\n", result);
  }

  @Test
  void ensureHashTestCaseWorks() {
    val result = Jdenticon.toSvg("35318264c9a98faf79965c270ac80c5606774df1");
    assertEquals("<svg\n"
                 + " xmlns=\"http://www.w3.org/2000/svg\"\n"
                 + " width=\"100\"\n"
                 + " height=\"100\"\n"
                 + " viewBox=\"0 0 100 100\"\n"
                 + " preserveAspectRatio=\"xMidYMid meet\"\n"
                 + "> <path\n"
                 + "  fill=\"#E6E6E6\"\n"
                 + "  d=\"M29 12a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M54 12a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M54 87a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M29 87a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M4 37a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M79 37a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M79 62a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0M4 62a8,8 0 1, 1 16,0a8,8 0 1, 1 -16,0\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + " <path\n"
                 + "  fill=\"#33995E\"\n"
                 + "  d=\"M0 0L25 0L25 25ZM100 0L100 25L75 25ZM100 100L75 100L75 75ZM0 100L0 75L25 75Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + " <path\n"
                 + "  fill=\"#66CC91\"\n"
                 + "  d=\"M25 25L50 25L50 50L25 50ZM41 47L47 35L35 35ZM75 25L75 50L50 50L50 25ZM52 41L65 47L65 35ZM75 75L50 75L50 50L75 50ZM58 52L52 65L65 65ZM25 75L25 50L50 50L50 75ZM47 58L35 52L35 65Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + "</svg>\n", result);
  }

  @Test
  void ensureSimpleStringWorks() {
    val result = Jdenticon.toSvg("Alice");

    assertEquals("<svg\n"
                 + " xmlns=\"http://www.w3.org/2000/svg\"\n"
                 + " width=\"100\"\n"
                 + " height=\"100\"\n"
                 + " viewBox=\"0 0 100 100\"\n"
                 + " preserveAspectRatio=\"xMidYMid meet\"\n"
                 + "> <path\n"
                 + "  fill=\"#E6E6E6\"\n"
                 + "  d=\"M37 0L50 12L37 25L25 12ZM75 12L62 25L50 12L62 0ZM62 100L50 87L62 75L75 87ZM25 87L37 75L50 87L37 100ZM12 25L25 37L12 50L0 37ZM100 37L87 50L75 37L87 25ZM87 75L75 62L87 50L100 62ZM0 62L12 50L25 62L12 75Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + " <path\n"
                 + "  fill=\"#4D4D4D\"\n"
                 + "  d=\"M0 25L0 0L25 0ZM75 0L100 0L100 25ZM100 75L100 100L75 100ZM25 100L0 100L0 75Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + " <path\n"
                 + "  fill=\"#D18975\"\n"
                 + "  d=\"M35 41a6,6 0 1, 1 12,0a6,6 0 1, 1 -12,0M53 41a6,6 0 1, 1 12,0a6,6 0 1, 1 -12,0M53 59a6,6 0 1, 1 12,0a6,6 0 1, 1 -12,0M35 59a6,6 0 1, 1 12,0a6,6 0 1, 1 -12,0\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + "</svg>\n", result);

  }

  @Test
  @SneakyThrows
  void ensureIdenticonWorks() {

    val baos = new ByteArrayOutputStream();
    Identicon.toSvg("Josiah Frapper").write(new XmlWriter(baos));
    baos.flush();
    val str = baos.toString(StandardCharsets.UTF_8);

    assertEquals("<svg\n"
                 + " xmlns=\"http://www.w3.org/2000/svg\"\n"
                 + " width=\"100\"\n"
                 + " height=\"100\"\n"
                 + " viewBox=\"0 0 100 100\"\n"
                 + " preserveAspectRatio=\"xMidYMid meet\"\n"
                 + "> <path\n"
                 + "  fill=\"#B3E6BF\"\n"
                 + "  d=\"M50 25L25 25L25 0ZM50 25L50 0L75 0ZM50 75L75 75L75 100ZM50 75L50 100L25 100ZM25 50L0 50L0 25ZM75 50L75 25L100 25ZM75 50L100 50L100 75ZM25 50L25 75L0 75Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + " <path\n"
                 + "  fill=\"#4D4D4D\"\n"
                 + "  d=\"M25 0L25 25L0 25ZM100 25L75 25L75 0ZM75 100L75 75L100 75ZM0 75L25 75L25 100Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + " <path\n"
                 + "  fill=\"#66CC80\"\n"
                 + "  d=\"M50 37L50 50L37 50ZM62 50L50 50L50 37ZM50 62L50 50L62 50ZM37 50L50 50L50 62Z\"\n"
                 + "  fill-opacity=\"1.0\"\n"
                 + " > </path>\n"
                 + "</svg>\n", str);
  }

}