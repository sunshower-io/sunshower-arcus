package io.sunshower.lang;

import static org.junit.jupiter.api.Assertions.assertTrue;

import io.sunshower.lang.Tests.Directories;
import org.junit.jupiter.api.Test;

class TestsTest {

  @Test
  void ensureDirectoryExists() {
    assertTrue(Tests.projectDirectory().exists());
  }

  @Test
  void ensureTestsCanResolveFile() {
    assertTrue(
        Tests.locateDirectory(Directories.TestResources, "sample-test", "test.xml").exists());
  }

  @Test
  void ensureShorthandWorksForResolution() {
    assertTrue(Tests.locateDirectory(Directories.TestResources, "sample-test/test.xml").exists());
  }
}
