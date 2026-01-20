package com.whatslovermbti.mbti_prj.security.oauth.userInfo;

import com.whatslovermbti.mbti_prj.constant.Provider;

public interface OAuthUserInfo {
    String getProviderId();
    String getEmail();
    String getName();
    Provider getProvider(); // GOOGLE / KAKAO / NAVER
}

