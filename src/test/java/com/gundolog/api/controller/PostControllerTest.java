package com.gundolog.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gundolog.api.entity.Post;
import com.gundolog.api.repository.PostRepository;
import com.gundolog.api.request.PostCreate;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

// @SpringBootTest를 달아주면 mockMvc는 안되고, WebMvcTest를 달아주면 Spring 오류가 나는 현상
// AutoConfigureMockMvc로 해결

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

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
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());

        // then
        Assertions.assertThat(postRepository.count()).isEqualTo(1);
    }
    // Test에서 생성되는 Repository는 무엇일까?
    // PostCreate에 생성자를 만들었더니 Mapping 에러가 발생

    @Test
    @DisplayName("/posts 요청시 title 값은 필수다")
    void test2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"content\": \"내용입니다.\"}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @DisplayName("/posts 요청시 content 값은 필수다")
    void test3() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"제목입니다.\"}"))
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("400"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("잘못된 요청입니다."))
            .andDo(MockMvcResultHandlers.print());
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
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(post.getId()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("foo"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.content").value("bar"))
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("/posts 요청 시 여러 글을 조회 할 수 있다.")
    void test5() throws Exception {
        // given
        Post post1 = Post.builder()
            .title("foo1")
            .content("bar")
            .build();

        Post post2 = Post.builder()
            .title("foo2")
            .content("bar")
            .build();
        postRepository.save(post1);
        postRepository.save(post2);

        // then
        mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.length()", Matchers.is(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("foo1"))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("foo2"))
            .andDo(MockMvcResultHandlers.print());
    }
}

// jsonPath() 메서드의 여러 함수
// Matchers()와 MockMvc는 어떻게 동작하는가?