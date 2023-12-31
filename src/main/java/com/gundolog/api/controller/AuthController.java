package com.gundolog.api.controller;

import com.gundolog.api.request.Login;
import com.gundolog.api.service.AuthService;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/login")
    public ResponseEntity<Object> login(@RequestBody Login login) {
        String accessToken = authService.signin(login);
        ResponseCookie cookie = ResponseCookie.from("SESSION",accessToken)
            .domain("localhost") // todo 서버 환경에 따른 분리 필요
            .path("/")
            .httpOnly(true)
            .secure(false)
            .maxAge(Duration.ofDays(30)) // 쿠키 유지 시간을 설정
            .sameSite("Strict") // 무엇을 의미하는가?
            .build();


        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, cookie.toString())
            .build();
    }
}
