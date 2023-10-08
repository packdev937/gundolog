package com.gundolog.api.exception;

public class InvalidSigninException extends GundologException {

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

    public InvalidSigninException() {
        super(MESSAGE);
    }

    public InvalidSigninException(Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
