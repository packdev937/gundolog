package com.gundolog.api.exception;

public class InvalidRequestException extends GundologException {

    private static final String MESSAGE = "잘못된 요청 입니다.";

    public InvalidRequestException() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
