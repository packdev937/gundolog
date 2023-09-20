package com.gundolog.api.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.gundolog.api.entity.Post;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import com.gundolog.api.request.PostEdit;
import com.gundolog.api.request.PostSearch;
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

        PostSearch postSearch = PostSearch.builder()
            .page(1)
            .size(10)
            .build();
        // when

        List<PostResponse> posts = postService.getList(
            postSearch); // application.yml에서 시작 을 1로 설정 가능
        // then
        assertEquals(10, posts.size());
        assertEquals("건돌로그 제목30", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("PageSearch에 값이 전달 안됐을 때는 기본 값으로 대체된다")
    void test5() {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
            .mapToObj(i -> {
                return Post.builder()
                    .title("건돌로그 제목" + i)
                    .content("내용" + i)
                    .build();
            }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        PostSearch postSearch = PostSearch.builder()
            .size(10)
            .build();

        // when
        List<PostResponse> posts = postService.getList(
            postSearch);

        // then
        assertEquals(10, posts.size());
        assertEquals("건돌로그 제목30", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("게시글 제목 수정")
    void test6() {
        // given
        Post post = Post.builder()
            .title("건돌로그")
            .content("내용내용")
            .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
            .title("호돌로그")
            .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
            .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. " + post.getId()));

        assertEquals("호돌로그", changedPost.getTitle());
    }

    @Test
    @DisplayName("게시글 내용 수정")
    void test7() {
        // given
        Post post = Post.builder()
            .title("건돌로그")
            .content("내용내용")
            .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
            .title("건돌로그")
            .content("용내용내")
            .build();

        // when
        postService.edit(post.getId(), postEdit);

        // then
        Post changedPost = postRepository.findById(post.getId())
            .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. " + post.getId()));

        assertEquals("건돌로그", changedPost.getTitle());
        assertEquals("용내용내", changedPost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test8() {
        // given
        Post post = Post.builder()
            .title("삭제될 제목")
            .content("삭제될 내용")
            .build();

        postRepository.save(post);

        // when
        postService.delete(post.getId());

        // tehn
        assertEquals(0, postRepository.count());
    }
}
