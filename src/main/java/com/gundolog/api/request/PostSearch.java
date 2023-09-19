package com.gundolog.api.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 20;

    public PostSearch(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public long getOffset() {
        return (long) (Math.max(this.page, 1) - 1) * Math.min(size, MAX_SIZE);
    }
}

// Pageable 을 안쓰는 이유 -> request Dto를 만들어서 size, page, 검색 조건 등 다양하게 설정할 수 있도록
// 유동적으로 가져가면서 사용자의 요구에 따라 (한페이지당 10개, 20개, 50개, 100개) 등 다양하게 설정해 줄 수 있음
