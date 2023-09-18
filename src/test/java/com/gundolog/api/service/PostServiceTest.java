package com.gundolog.api.service;


import static org.junit.jupiter.api.Assertions.*;

import com.gundolog.api.entity.Post;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import com.gundolog.api.response.PostResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

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
        assertEquals("foo", findPost.getTitle());
        assertEquals("bar", findPost.getContent());
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
        assertEquals("1234567891", response.getTitle());
        assertEquals("bar", response.getContent());
    }

    @Test
    @DisplayName("글 첫 1페이지 조회")
    void test4() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
            .mapToObj(i -> {
                return Post.builder()
                    .title("건돌로그 제목" + i)
                    .content("내용" + i)
                    .build();
            }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        Pageable pageable = PageRequest.of(0,5, Sort.by(Direction.DESC,"id"));
        // when

        List<PostResponse> posts = postService.getList(pageable); // application.yml에서 시작 을 1로 설정 가능
        // then
        assertEquals(5, posts.size());
        assertEquals("건돌로그 제목30", posts.get(0).getTitle());
    }
}

// Controller -> WebPostService (Response를 위한 서비스) -> Repository
//               PostService (다른 서비스와 통신하기 위한 서비스)

// 클래스에 대한 분리
// Request, Response

// 하드 코딩 절대하지마 --> postService.get(1L) (X) -> postService.get(post.getId()) (O)

// Pageable 객체