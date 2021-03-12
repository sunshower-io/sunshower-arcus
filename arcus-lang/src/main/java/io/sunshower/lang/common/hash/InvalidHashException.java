package io.sunshower.lang.common.hash;

public class InvalidHashException extends RuntimeException {

  public InvalidHashException() {}

  public InvalidHashException(String message) {
    super(message);
  }

  public InvalidHashException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidHashException(Throwable cause) {
    super(cause);
  }

  public InvalidHashException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
