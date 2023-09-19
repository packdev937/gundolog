package com.gundolog.api.request;

import lombok.Data;

@Data
public class PostSearch {

    private int page;
    private int size;
}

// Pageable 을 안쓰는 이유 -> request Dto를 만들어서 size, page, 검색 조건 등 다양하게 설정할 수 있도록
