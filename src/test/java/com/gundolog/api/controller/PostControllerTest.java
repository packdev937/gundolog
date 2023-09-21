package com.gundolog.api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gundolog.api.entity.Post;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import com.gundolog.api.request.PostEdit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

// @SpringBootTest를 달아주면 mockMvc는 안되고, WebMvcTest를 달아주면 Spring 오류가 나는 현상
// AutoConfigureMockMvc로 해결

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // 다른 테스트의 수행에 의해 테스트 결과가 달라지면 안되기 때문에
    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청 시 DB에 값이 저장된다")
    void test1() throws Exception {
        // Given
        PostCreate request = new PostCreate();
        request.setTitle("제목입니다");
        request.setContent("내용입니다");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        // when
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andDo(print());

        // then
        Assertions.assertThat(postRepository.count()).isEqualTo(1);
    }
    // Test에서 생성되는 Repository는 무엇일까?
    // PostCreate에 생성자를 만들었더니 Mapping 에러가 발생

    @Test
    @DisplayName("/posts 요청시 title 값은 필수다")
    void test2() throws Exception {
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\": \"내용입니다.\"}"))
            .andExpect(status().isBadRequest())
            .andDo(print());

    }

    @Test
    @DisplayName("/posts 요청시 content 값은 필수다")
    void test3() throws Exception {
        mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"제목입니다.\"}"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("400"))
            .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
            .andDo(print());
    }

    @Test
    @DisplayName("/posts/{postId} 요청시 글을 조회할 수 있다")
    void test4() throws Exception {
        // given
        Post post = Post.builder()
            .title("foo")
            .content("bar")
            .build();
        postRepository.save(post);

        // then
        mockMvc.perform(get("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(post.getId()))
            .andExpect(jsonPath("$.title").value("foo"))
            .andExpect(jsonPath("$.content").value("bar"))
            .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청 시 여러 글을 조회 할 수 있다.")
    void test5() throws Exception {
        // given
        List<Post> requestPosts = IntStream.range(1, 31)
            .mapToObj(i -> {
                return Post.builder()
                    .title("건돌로그 제목" + i)
                    .content("내용 " + i)
                    .build();
            }).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        // then
        mockMvc.perform(get("/posts?page=1&size=10")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value((10)))
            .andExpect(jsonPath("$[0].title").value("건돌로그 제목30"))
            .andExpect(jsonPath("$[0].content").value("내용 30"))
            .andDo(print());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test6() throws Exception {
        // given
        Post post = Post.builder()
            .title("건돌로그")
            .content("내용내용")
            .build();
        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
            .title("건돌로그")
            .content("가나다라")
            .build();

        // then
        mockMvc.perform(patch("/posts/{postID}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postEdit)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("건돌로그"))
            .andExpect(jsonPath("$.content").value("가나다라"))
            .andDo(print());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test7() throws Exception {
        // given
        Post post = Post.builder()
            .title("건돌로그")
            .content("가나다라")
            .build();
        postRepository.save(post);

        // then
        mockMvc.perform(delete("/posts/{postID}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 삭제")
    void test8() throws Exception {
        mockMvc.perform(delete("/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test9() throws Exception {
        // given
        PostEdit postEdit = PostEdit.builder()
            .title("건돌로그")
            .content("가나다라")
            .build();

        // then
        mockMvc.perform(patch("/posts/{postId}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(postEdit)))
            .andExpect(status().isNotFound())
            .andDo(print());
    }
}

// jsonPath() 메서드의 여러 함수
// Matchers()와 MockMvc는 어떻게 동작하는가?
// ObjectMapper와 직렬화 https://da-nyee.github.io/posts/woowacourse-why-the-default-constructor-is-needed/

// 자바의 직렬화와 역직렬화
// NoArgsConstructor가 있으니간 에러가 안난다.