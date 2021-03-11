package io.sunshower.arcus.config;

import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import lombok.val;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigurationLoader {
    static final Logger log = LogManager.getLogger(ConfigurationLoader.class);

    public static <T> T load(Class<T> type, Reader reader, String mimetype) throws Exception {
        return load(Thread.currentThread().getContextClassLoader(), type, reader, mimetype);
    }

    public static <T> T load(ClassLoader classLoader, Class<T> type, Reader reader, String mimeType)
            throws Exception {
        log.info("Attempting to load reader for format '{}' (type={})", mimeType, type);
        val loader = ServiceLoader.load(ConfigurationReader.class, classLoader).iterator();
        while (loader.hasNext()) {
            val next = loader.next();
            log.info("Located configuration reader: {} ", next);
            if (next.handles(mimeType)) {
                log.info(
                        "Successfully located configuration reader applicable to {}: {}",
                        mimeType,
                        next);
                return next.read(type, reader);
            } else {
                log.info("Reader {} was not applicable to {}", next, mimeType);
            }
        }
        log.error("Failed to locate any reader for format: {}", mimeType);
        throw new NoSuchElementException("Failed to resolve service of type: ");
    }
}
