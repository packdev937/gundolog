package com.gundolog.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
public class PostResponse {

    private final Long id;
    private final String title;
    private final String content;

    // 정책에 맞는 반환 값 설정
    public String getTitle() {
        return this.title.substring(0, Math.min(this.title.length(), 10));
    }

    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
