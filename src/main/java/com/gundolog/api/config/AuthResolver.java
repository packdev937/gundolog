package com.gundolog.api.config;

import com.gundolog.api.config.data.UserSession;
import com.gundolog.api.entity.Session;
import com.gundolog.api.exception.UnAuthorizedException;
import com.gundolog.api.repository.SessionRepository;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        if (servletRequest == null) {
            log.error("servletRequest null");
            throw new UnAuthorizedException("권한이 없습니다.");
        }
        Cookie[] cookies = servletRequest.getCookies();
        if (cookies.length == 0) {
            log.error("no cookie");
            throw new UnAuthorizedException("권한이 없습니다.");
        }

        String accessToken = cookies[0].getValue();
        
        Session session = sessionRepository.findByAccessToken(accessToken)
            .orElseThrow(() -> new UnAuthorizedException("권한이 없습니다."));

        return new UserSession(session.getUser().getId());
    }
}
