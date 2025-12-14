package com.whatslovermbti.mbti_prj.security.oauth;

import com.whatslovermbti.mbti_prj.security.oauth.userInfo.GoogleOAuthUserInfo;
import com.whatslovermbti.mbti_prj.security.oauth.userInfo.KakaoOAuthUserInfo;
import com.whatslovermbti.mbti_prj.security.oauth.userInfo.NaverOAuthUserInfo;
import com.whatslovermbti.mbti_prj.security.oauth.userInfo.OAuthUserInfo;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService
        extends DefaultOAuth2UserService {

    private final UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 1. OAuth Provider로부터 사용자 정보 수신
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 2. 어떤 Provider인지 확인 (google / kakao / naver)
        String registrationId =
                userRequest
                        .getClientRegistration()
                        .getRegistrationId(); // google / kakao / naver

        OAuthUserInfo oAuthUserInfo = createOAuthUserInfo(registrationId, attributes);


        // 3. 회원가입 or 로그인 처리
        User user = userService.signupOrLoginOAuth(oAuthUserInfo);

        // 4. Spring Security가 이해할 수 있는 OAuth2User 반환
        return new CustomOAuth2User(user, attributes);
    }

    private OAuthUserInfo createOAuthUserInfo(
            String registrationId,
            Map<String, Object> attributes
    ) {
        return switch (registrationId) {
            case "google" -> new GoogleOAuthUserInfo(attributes);
            case "kakao"  -> new KakaoOAuthUserInfo(attributes);
            case "naver"  -> new NaverOAuthUserInfo(attributes);
            default -> throw new IllegalArgumentException("Unsupported OAuth provider");
        };
    }
}