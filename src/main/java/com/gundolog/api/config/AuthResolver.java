package com.gundolog.api.config;

import com.gundolog.api.config.data.UserSession;
import com.gundolog.api.exception.UnAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class AuthResolver implements HandlerMethodArgumentResolver {

    // 넘어온 parameter가 UserSession이 맞아?
    // DTO가 반드시 인증이 필요한 라우터다 ->
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization");
        if (accessToken == null || accessToken.equals("")) {
            throw new UnAuthorizedException("권한이 없습니다.");
        }
        UserSession userSession = new UserSession();
        userSession.id = 1L;
        return userSession;
    }
}
