package io.sunshower.arcus.config;

public class DuplicateConfigurationException extends ConfigurationException {

  public DuplicateConfigurationException() {
    super();
  }

  public DuplicateConfigurationException(String message) {
    super(message);
  }

  public DuplicateConfigurationException(String message, Throwable cause) {
    super(message, cause);
  }

  public DuplicateConfigurationException(Throwable cause) {
    super(cause);
  }

  protected DuplicateConfigurationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
