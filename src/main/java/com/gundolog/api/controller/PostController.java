package com.gundolog.api.controller;

import com.gundolog.api.request.PostCreate;
import com.gundolog.api.request.PostSearch;
import com.gundolog.api.response.PostResponse;
import com.gundolog.api.service.PostService;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate postCreate) {
        postService.write(postCreate);
        return;
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable(name = "postId") Long id) {
        return postService.get(id);
    }

    // 글이 너무 많 경우 -> 비용이 많이 들고, DB글 모두를 조회하는 경우 DB가 뻗을 수 있으며, 시간, 트래픽 비용등이 많이 발생한다
    @GetMapping("/posts")
    public List<PostResponse> getList(@RequestParam PostSearch postSearch){
        return postService.getList(postSearch);
    }
}

// @RequestParam과 @PathVariable의 차이