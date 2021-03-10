package io.sunshower.persistence;

public class UnsupportedDatabaseException extends RuntimeException {

  public UnsupportedDatabaseException() {
    super();
  }

  public UnsupportedDatabaseException(String message) {
    super(message);
  }

  public UnsupportedDatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnsupportedDatabaseException(Throwable cause) {
    super(cause);
  }

  protected UnsupportedDatabaseException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
