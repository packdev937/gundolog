package com.gundolog.response;

/*
* {
*   "code" : "400",
*   "message" : "잘못된 요청입니다."
*   "validation" : {
*       "title" : "제목을 입력해주세요."
*    }
* }
* */

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;
}

// 에러를 정의하는 다양한 방
