package com.gundolog.api.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    // 수정을 할 수 있는 필드에 한해서 속성 값을 만들어준다
    private final String title;
    private final String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }
}

// 생성자 Builder와 클래스 Builder의 차이
