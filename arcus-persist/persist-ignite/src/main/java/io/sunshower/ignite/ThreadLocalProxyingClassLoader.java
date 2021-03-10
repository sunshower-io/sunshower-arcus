package io.sunshower.ignite;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Enumeration;
import lombok.val;
import org.jetbrains.annotations.Nullable;

public class ThreadLocalProxyingClassLoader extends ClassLoader implements Serializable {

  final ClassLoader delegate;

  public ThreadLocalProxyingClassLoader(ClassLoader delegate) {
    this.delegate = delegate;
  }

  @Override
  public Class<?> loadClass(String name) throws ClassNotFoundException {
    try {
      return classloader().loadClass(name);
    } catch (ClassNotFoundException ex) {
      return delegate.loadClass(name);
    }
  }

  @Nullable
  @Override
  public URL getResource(String name) {
    val resource = classloader().getResource(name);
    if (resource == null) {
      return delegate.getResource(name);
    }
    return resource;
  }

  @Override
  public Enumeration<URL> getResources(String name) throws IOException {
    Enumeration<URL> values = null;
    try {
      values = classloader().getResources(name);
    } catch (IOException ex) {
      values = delegate.getResources(name);
    }
    return values;
  }

  @Nullable
  @Override
  public InputStream getResourceAsStream(String name) {
    InputStream is = classloader().getResourceAsStream(name);
    if (is == null) {
      return delegate.getResourceAsStream(name);
    }
    return is;
  }

  @Override
  public void setDefaultAssertionStatus(boolean enabled) {
    classloader().setDefaultAssertionStatus(enabled);
  }

  @Override
  public void setPackageAssertionStatus(String packageName, boolean enabled) {
    classloader().setPackageAssertionStatus(packageName, enabled);
  }

  @Override
  public void setClassAssertionStatus(String className, boolean enabled) {
    classloader().setClassAssertionStatus(className, enabled);
  }

  @Override
  public void clearAssertionStatus() {
    classloader().clearAssertionStatus();
  }

  static ClassLoader classloader() {
    return Thread.currentThread().getContextClassLoader();
  }
}
