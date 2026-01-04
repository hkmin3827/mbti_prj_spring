package com.whatslovermbti.mbti_prj.handler;

import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.security.jwt.JwtProvider;
import com.whatslovermbti.mbti_prj.security.oauth.userInfo.GoogleOAuthUserInfo;
import com.whatslovermbti.mbti_prj.security.oauth.userInfo.KakaoOAuthUserInfo;
import com.whatslovermbti.mbti_prj.security.oauth.userInfo.NaverOAuthUserInfo;
import com.whatslovermbti.mbti_prj.security.oauth.userInfo.OAuthUserInfo;
import com.whatslovermbti.mbti_prj.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        // Provider 판별 + OAuthUserInfo 생성
        OAuthUserInfo oAuthUserInfo = resolveOAuthUserInfo(attributes);

        // 이미 검증된 OAuth 정보로 User 생성 or 조회
        User user = authService.signupOrLoginOAuth(oAuthUserInfo);

        // userId 기준 JWT 발급
        String token = jwtProvider.createToken(user.getId());
        String redirectUrl =
                "http://localhost:5173/oauth/callback?token=" + token;

        getRedirectStrategy().sendRedirect(request, response, redirectUrl);

    }

    /**
     * provider별 OAuthUserInfo 매핑
     * OIDC 여부와 완전히 분리됨
     */
    private OAuthUserInfo resolveOAuthUserInfo(Map<String, Object> attributes) {

        // GOOGLE (OIDC 포함)
        if (attributes.containsKey("sub")) {
            return new GoogleOAuthUserInfo(attributes);
        }

        // KAKAO
        if (attributes.containsKey("kakao_account")) {
            return new KakaoOAuthUserInfo(attributes);
        }

        // NAVER
        if (attributes.containsKey("response")) {
            return new NaverOAuthUserInfo(attributes);
        }

        throw new IllegalStateException("Unsupported OAuth provider");
    }
}