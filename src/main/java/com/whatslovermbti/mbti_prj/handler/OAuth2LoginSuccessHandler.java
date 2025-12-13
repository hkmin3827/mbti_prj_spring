package com.whatslovermbti.mbti_prj.handler;

import com.whatslovermbti.mbti_prj.security.jwt.JwtProvider;
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

    @SuppressWarnings("unchecked")
    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException {

        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oauth2User.getAttributes();

        String provider;
        String providerId;
        String email = null;
        String nickname = null;

        // ✅ 카카오
        if (attributes.containsKey("kakao_account")) {
            provider = "KAKAO";
            providerId = attributes.get("id").toString();

            Map<String, Object> kakaoAccount =
                    (Map<String, Object>) attributes.get("kakao_account");

            Map<String, Object> profile =
                    (Map<String, Object>) kakaoAccount.get("profile");

            nickname = (String) profile.get("nickname");
            email = (String) kakaoAccount.get("email"); // 대부분 null
        }
        else if (attributes.containsKey("response")) {
            provider = "NAVER";

            Map<String, Object> responseMap =
                    (Map<String, Object>) attributes.get("response");

            providerId = responseMap.get("id").toString();
            email = (String) responseMap.get("email");
            nickname = (String) responseMap.get("name");
        }
        // ✅ 구글
        else{
            provider = "GOOGLE";
            providerId = attributes.get("sub").toString();
            email = (String) attributes.get("email");
            nickname = (String) attributes.get("name");
        }

        // 🔑 OAuth 전용 JWT 발급 (provider + providerId 기준)
        String token = jwtProvider.createOAuthToken(provider, providerId);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("""
            {
              "provider": "%s",
              "providerId": "%s",
              "nickname": "%s",
              "token": "%s"
            }
        """.formatted(provider, providerId, nickname, token));
    }
}
