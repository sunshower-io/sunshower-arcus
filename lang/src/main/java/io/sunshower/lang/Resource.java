package io.sunshower.lang;

import java.io.File;
import java.net.URI;
import java.net.URL;

/**
 * Created by haswell on 3/29/16.
 */
public interface Resource {

    long length();

    Resource get(String relativePath);

    boolean exists();

    String getDescription();

    File getFile();

    String getFilename();

    URI getURI();

    URL getURL();

    boolean isOpen();

    boolean isReadable();

    long lastModified();
}
