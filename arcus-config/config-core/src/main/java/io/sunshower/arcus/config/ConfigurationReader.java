package io.sunshower.arcus.config;

import io.sunshower.lang.tuple.Pair;
import java.io.InputStream;
import java.io.Reader;
import java.util.Set;

public interface ConfigurationReader {

  /**
   * return a set of tuples whose first entry is the extension and whose second entry is the
   * mime-type
   */
  Set<Pair<String, String>> knownFileTypes();

  /**
   *
   * @param format
   * @return
   */
  boolean handles(String format);


  /**
   *
   * @param type
   * @param reader
   * @param <T>
   * @return
   * @throws Exception
   */
  <T> T read(Class<T> type, Reader reader) throws Exception;

  /**
   *
   * @param type
   * @param reader
   * @param <T>
   * @return
   * @throws Exception
   */
  <T> T read(Class<T> type, InputStream reader) throws Exception;
}
