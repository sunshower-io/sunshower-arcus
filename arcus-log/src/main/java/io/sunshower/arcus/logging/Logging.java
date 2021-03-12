package io.sunshower.arcus.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

public class Logging {

  static final Logger logger = LogManager.getLogger(Logging.class);
  public static void setLevel(Class<?> name, Level level) {
    setLevel(name.getCanonicalName(), level);
  }

  public static void setLevel(String name, Level level) {
    logger.info("Setting logging for package: {} to level {}", name, level);
    Configurator.setLevel(name, level);
    logger.info("Successfully set level for package: {} to level {}", name, level);
  }
}
