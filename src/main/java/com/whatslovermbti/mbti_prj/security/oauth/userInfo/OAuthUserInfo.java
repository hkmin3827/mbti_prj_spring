package com.whatslovermbti.mbti_prj.security.oauth.userInfo;

import com.whatslovermbti.mbti_prj.constant.Provider;

// 외부 OAuth Provider 응답을 표준화하는 Adapter 인터페이스
// 원래 dto가 아닌 infra 폴더에 client로 이 UserInfo들을 받기도 하나, 현재는 dto에서 파싱
public interface OAuthUserInfo {
    String getProviderId();
    String getEmail();
    String getName();
    Provider getProvider(); // GOOGLE / KAKAO / NAVER
}

