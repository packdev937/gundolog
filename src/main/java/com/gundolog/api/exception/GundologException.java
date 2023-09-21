package com.gundolog.api.exception;

public abstract class GundologException extends RuntimeException {

    public GundologException(String message) {
        super(message);
    }

    public GundologException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();
}
