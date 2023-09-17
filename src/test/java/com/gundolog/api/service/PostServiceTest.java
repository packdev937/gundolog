package com.gundolog.api.service;

import static org.junit.jupiter.api.Assertions.*;

import com.gundolog.api.entity.Post;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import com.gundolog.api.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void clear() {
        postRepository.deleteAll();
    }

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

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        // given
        Post post = Post.builder()
            .title("foo")
            .content("bar")
            .build();

        postRepository.save(post);

        // when
        PostResponse response = postService.get(1L);

        // then
        org.junit.jupiter.api.Assertions.assertEquals("foo", response.getTitle());
        org.junit.jupiter.api.Assertions.assertEquals("bar", response.getContent());
    }
}

// getTitle() -> subString(0,10)
// 후에 다른 기능을 만드는데 getTitle()에서 위의 정책이 적용되면 문제가 된다
// Getter에는 서비스의 정책을 넣지 말라
// 응답 전용 클래스를 만들어주는 것이 좋다