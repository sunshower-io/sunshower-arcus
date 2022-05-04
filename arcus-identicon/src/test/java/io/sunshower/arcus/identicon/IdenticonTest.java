package io.sunshower.arcus.identicon;


import io.sunshower.arcus.markup.writers.XmlWriter;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;

class IdenticonTest {

  @Test
  @SneakyThrows
  void ensureIdenticonWorks() {

    val baos = new ByteArrayOutputStream();
    Identicon.toSvg("Josiah Frapper").write(new XmlWriter(baos));
    baos.flush();
    val str = baos.toString(StandardCharsets.UTF_8);
    System.out.println(str);
  }

}