package io.sunshower.arcus.lang.test;

import static java.lang.String.format;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.val;

public class Tests {

  static final FilenameFilter KNOWN_FILE_FILTER = new KnownFileFilter();
  static final Set<KnownFile> MARKER_FILES =
      Set.of(
          new KnownFile("build.gradle", Type.File),
          new KnownFile("pom.xml", Type.File),
          new KnownFile("build", Type.Directory),
          new KnownFile("target", Type.Directory),
          new KnownFile("gradle.properties", Type.File));

  public static File projectFile(String... paths) {
    return Paths.get(projectPath().toString(), paths).toFile();
  }

  public static Path locateDirectoryPath(Directories directories, String... rest) {
    for (val directory : directories.aliases) {
      val test = projectFile(directory.split("/"));
      if (test.exists()) {
        return Paths.get(test.toPath().toString(), rest);
      }
    }
    throw new NoSuchElementException("Unable to locate " + directories);
  }

  public static File locateDirectory(Directories directories, String... rest) {
    return locateDirectoryPath(directories, rest).toFile();
  }

  /** @return the project directory as a path */
  public static Path projectPath() {
    return projectDirectory().toPath();
  }

  /**
   * Look up the directory hierarchy until you can a src directory as a child, or a build directory,
   * or a target directory, or a build.gradle, or a pom.xml
   *
   * @return the project directory
   * @throws IllegalArgumentException if there is no marker directory
   */
  @SuppressFBWarnings
  public static File projectDirectory() {

    val callerElement = externalStackTraceElement();
    try {

      val callerType =
          Thread.currentThread().getContextClassLoader().loadClass(callerElement.getClassName());
      var currentFile =
          new File(callerType.getProtectionDomain().getCodeSource().getLocation().getFile());
      for (; currentFile != null; currentFile = currentFile.getParentFile()) {
        if (currentFile.list(KNOWN_FILE_FILTER).length > 0) {
          return currentFile;
        }
      }
    } catch (ClassNotFoundException ex) {
      throw new IllegalArgumentException(
          format("Error: class '%s' not found in classloader", callerElement.getClassName()));
    }
    throw new IllegalArgumentException(
        format("Could not determine the location of a caller: %s", callerElement.getClassName()));
  }

  static StackTraceElement externalStackTraceElement() {
    val stackTrace = Thread.currentThread().getStackTrace();
    for (int i = 0; i < stackTrace.length; i++) {
      val className = stackTrace[i].getClassName();
      if (!(className.startsWith("java.lang") || className.equals(Tests.class.getName()))) {
        return stackTrace[i];
      }
    }
    throw new IllegalStateException("Never escaped this class for some reason");
  }

  public static Tests getInstance() {
    return Holder.INSTANCE;
  }

  public enum Directories {
    Resources("build/resources/main", "src/main/resources"),
    TestResources("build/resources/test", "src/test/resources");
    final String[] aliases;

    Directories(final String... aliases) {
      this.aliases = aliases;
    }
  }

  enum Type {
    File {
      @Override
      boolean matches(java.io.File file) {
        return file.isFile();
      }
    },
    Directory {
      @Override
      boolean matches(java.io.File file) {
        return file.isDirectory();
      }
    };

    abstract boolean matches(File file);
  }

  static class KnownFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String name) {
      val file = new File(dir, name);
      return MARKER_FILES.stream().anyMatch(t -> t.matches(file));
    }
  }

  static class KnownFile implements FilenameFilter {

    private final String name;
    private final Type type;

    KnownFile(String name, Type type) {
      this.name = name;
      this.type = type;
    }

    public boolean matches(File file) {
      return file.getName().equals(name) && file.exists() && type.matches(file);
    }

    @Override
    public boolean accept(File dir, String name) {
      return matches(new File(dir, name));
    }
  }

  static final class Holder {

    static final Tests INSTANCE = new Tests();
  }
}
