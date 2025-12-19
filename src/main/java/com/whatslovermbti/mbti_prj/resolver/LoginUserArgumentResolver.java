package com.whatslovermbti.mbti_prj.resolver;

import com.whatslovermbti.mbti_prj.annotation.LoginUser;
import com.whatslovermbti.mbti_prj.constant.ErrorCode;
import com.whatslovermbti.mbti_prj.exception.CustomException;
import com.whatslovermbti.mbti_prj.security.auth.CustomUserDetails;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.context.request.NativeWebRequest;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        // @LoginUser 붙어있고 파라미터 타입이 Long일 때만 처리
        return parameter.hasParameterAnnotation(LoginUser.class)
                && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getUser().getId(); // 로그인 유저의 userId 반환
    }
}