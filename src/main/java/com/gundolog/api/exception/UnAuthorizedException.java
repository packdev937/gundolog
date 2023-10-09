package com.gundolog.api.exception;

public class UnAuthorizedException extends GundologException {

    public UnAuthorizedException(String message) {
        super(message);
    }

    public UnAuthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}
