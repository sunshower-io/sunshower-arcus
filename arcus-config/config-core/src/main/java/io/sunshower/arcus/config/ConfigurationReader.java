package io.sunshower.arcus.config;

import java.io.Reader;

public interface ConfigurationReader {

    boolean handles(String format);

    <T> T read(Class<T> type, Reader reader) throws Exception;
}
