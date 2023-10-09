package com.gundolog.api.service;

import com.gundolog.api.entity.Session;
import com.gundolog.api.entity.User;
import com.gundolog.api.exception.InvalidSigninException;
import com.gundolog.api.repository.UserRepository;
import com.gundolog.api.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signin(Login request) {
        User user = userRepository.findByEmailAndPassword(request.getEmail(), request.getPassword())
            .orElseThrow(() -> new InvalidSigninException());

        // 로그인 성공 시 세션 발급, accessToken은 UUID
        Session session = user.addSession();

        return session.getAccessToken();
    }
}
