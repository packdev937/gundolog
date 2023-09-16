package com.gundolog.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("글 적성")
    void test1() {
        // given
        PostCreate postCreate = new PostCreate();
        postCreate.setTitle("제목");
        postCreate.setContent("내용");

        // when
        postService.write(postCreate);

        // then
        Assertions.assertThat(postRepository.count()).isEqualTo(1L);
    }
}