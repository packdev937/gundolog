package com.gundolog.api.controller;

import com.gundolog.api.exception.GundologException;
import com.gundolog.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse methodArgumentNotValidExceptionHandler(
        MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.builder()
            .code("400")
            .message("잘못된 요청입니다.")
            .build();

        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    @ResponseBody
    @ExceptionHandler(GundologException.class)
    public ResponseEntity<ErrorResponse> postNotFoundExceptionHandler(GundologException e) {
        ErrorResponse body = ErrorResponse.builder()
            .code(String.valueOf(e.statusCode()))
            .message(e.getMessage())
            .build();

        ResponseEntity<ErrorResponse> response = ResponseEntity.status(e.statusCode())
            .body(body);

        return response;
    }
}

// ControllerAdvice란?

// ResponseEntity로 감싸주면 원하는 status code를 설정해줄 수 있음