package com.gundolog.api.response;

/*
* {
*   "code" : "400",
*   "message" : "잘못된 요청입니다."
*   "validation" : {
*       "title" : "제목을 입력해주세요."
*    }
* }
* */

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String, String> validation = new HashMap<>();

    public void addValidation(String filedName, String errorMessage){
        this.validation.put(filedName, errorMessage);
    }

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }
}

// 에러를 정의하는 다양한 방법
// Map을 사용하는 것은 지양하는 것이 좋다.
