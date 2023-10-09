package com.gundolog.api.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gundolog.api.entity.User;
import com.gundolog.api.repository.SessionRepository;
import com.gundolog.api.repository.UserRepository;
import com.gundolog.api.request.Login;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
        sessionRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    void test1() throws Exception {
        // given
        userRepository.save(User.builder()
            .email("test@test.com")
            .name("test")
            .password("1234")
            .build());

        // when
        Login login = Login.builder()
            .email("test@test.com")
            .password("1234")
            .build();

        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andDo(print());
    }

    // 좋은 테스트는 아니다
    @Test
    @DisplayName("로그인 성공 후 세션 응답")
    void test2() throws Exception {
        // given
        userRepository.save(User.builder()
            .email("test@test.com")
            .name("test")
            .password("1234")
            .build());

        // when
        Login login = Login.builder()
            .email("test@test.com")
            .password("1234")
            .build();

        String json = objectMapper.writeValueAsString(login);

        mockMvc.perform(post("/auth/login")
                .contentType(APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
            .andDo(print());

    }
}