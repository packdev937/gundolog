package com.gundolog.api.service;


import com.gundolog.api.entity.Post;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import com.gundolog.api.response.PostResponse;
import java.time.LocalDateTime;
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
    @DisplayName("repository에 저장한 내용과 저장된 내용이 일치한다")
    void test2() {
        // given
        Post post = Post.builder()
            .title("foo")
            .content("bar")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        // when
        postRepository.save(post);

        // then
        Post findPost = postRepository.findById(post.getId()).get();
        System.out.println(post.getId());
        org.junit.jupiter.api.Assertions.assertEquals("foo", findPost.getTitle());
        org.junit.jupiter.api.Assertions.assertEquals("bar", findPost.getContent());
    }

    @Test
    @DisplayName("제목의 길이가 10 미만 이여야 합니다.")
    void test3() {
        // given
        Post post = Post.builder()
            .title("1234567891234")
            .content("bar")
            .createdDate(LocalDateTime.now())
            .updatedDate(LocalDateTime.now())
            .build();

        // when
        postRepository.save(post);

        // when
        PostResponse response = postService.get(post.getId());

        // then
        org.junit.jupiter.api.Assertions.assertEquals("1234567891", response.getTitle());
        org.junit.jupiter.api.Assertions.assertEquals("bar", response.getContent());
    }
}

// 응답 클래스를 만들어야 하는 이유 (1)
// getTitle() -> subString(0,10)
// 후에 다른 기능을 만드는데 getTitle()에서 위의 정책이 적용되면 문제가 된다
// Getter에는 서비스의 정책을 넣지 말라
// 응답 전용 클래스를 만들어주는 것이 좋다

// Controller -> WebPostService (Response를 위한 서비스) -> Repository
//               PostService (다른 서비스와 통신하기 위한 서비스)

// 클래스에 대한 분리
// Request, Response

// 하드 코딩 절대하지마 --> postService.get(1L) (X) -> postService.get(post.getId()) (O)