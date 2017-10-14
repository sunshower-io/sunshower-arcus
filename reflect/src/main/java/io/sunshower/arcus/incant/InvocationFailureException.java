package io.sunshower.arcus.incant;

/**
 * Created by haswell on 4/10/16.
 */
public class InvocationFailureException extends RuntimeException {

    public InvocationFailureException() {
        super();
    }

    public InvocationFailureException(String message) {
        super(message);
    }

    public InvocationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvocationFailureException(Throwable cause) {
        super(cause);
    }

    protected InvocationFailureException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
