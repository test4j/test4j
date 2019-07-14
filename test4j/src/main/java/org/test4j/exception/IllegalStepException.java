package org.test4j.exception;

public class IllegalStepException extends RuntimeException {

    public IllegalStepException() {
    }

    public IllegalStepException(String message) {
        super(message);
    }

    public IllegalStepException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalStepException(Throwable cause) {
        super(cause);
    }

    public IllegalStepException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
