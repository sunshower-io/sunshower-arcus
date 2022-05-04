package io.sunshower.arcus.identicon;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicReference;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class JdenticonTest {

  static int count = 0;

  @Disabled("don't want to recreate the files")
  @ParameterizedTest
  @ValueSource(
      strings = {
        "hello world",
        "Josiah Haswell",
        "another-test",
        "and I think to myself, what a porgly world!"
      })
  void writeSamples(String value) {
    val matching = new File(locateFile(), "test/resources");

    var cfg = Configuration.defaultBuilder().build();
    writeFile(matching, "default-" + count, Jdenticon.toSvg(value, cfg));

    cfg = Configuration.defaultBuilder().withOpacity(0.8f).withSaturation(0.9f).build();

    writeFile(matching, "opacity-sat-bg-" + count, Jdenticon.toSvg(value, cfg));

    cfg =
        Configuration.defaultBuilder()
            .withOpacity(0.8f)
            .withSaturation(0.7f)
            .withPadding(2)
            .build();

    writeFile(matching, "padding-opacity-saturation" + count, Jdenticon.toSvg(value, cfg));
    count++;
  }

  @SneakyThrows
  private void writeFile(File parent, String child, String svg) {
    val file = new File(parent, child + ".svg").toPath();
    try (val inputStream = new ByteArrayInputStream(svg.getBytes(StandardCharsets.UTF_8))) {
      Files.copy(inputStream, file, StandardCopyOption.REPLACE_EXISTING);
    }
  }

  private File locateFile() {
    val location = new AtomicReference<>(new File(ClassLoader.getSystemResource(".").getFile()));
    File[] matching = null;
    while (matching == null || matching.length == 0) {
      val actualLocation = location.get();
      matching = actualLocation.listFiles(f -> new File(actualLocation, "src").exists());
      if (matching != null && matching.length > 0) {
        for (val r : matching) {
          if (r.getName().equals("src")) {
            return r;
          }
        }
      }
      location.set(actualLocation.getParentFile());
    }
    throw new NoSuchElementException("No file");
  }
}
