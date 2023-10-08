package com.gundolog.api.service;

import com.gundolog.api.entity.User;
import com.gundolog.api.exception.InvalidSigninException;
import com.gundolog.api.repository.UserRepository;
import com.gundolog.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public void signin(Login request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
            .orElseThrow(() -> new InvalidSigninException());

        // 로그인 성공 시 세션 발급
    }
}
