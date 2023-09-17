package com.gundolog.api.response;

import com.gundolog.api.entity.Post;
import lombok.Builder;
import lombok.Getter;

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

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
