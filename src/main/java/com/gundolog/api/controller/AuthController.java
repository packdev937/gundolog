package com.gundolog.api.controller;

import com.gundolog.api.entity.User;
import com.gundolog.api.exception.InvalidSigninException;
import com.gundolog.api.repository.UserRepository;
import com.gundolog.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    @PostMapping("/auth/login")
    public User login(@RequestBody Login login) {
        // Json Check
        log.info(">>>{}", login);

        // DB에서 조회
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
            .orElseThrow(() -> new InvalidSigninException());

        return user;
    }
}
