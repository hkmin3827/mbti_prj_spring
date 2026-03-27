package com.whatslovermbti.mbti_prj.global.security.oauth.userInfo;

import com.whatslovermbti.mbti_prj.global.constant.Provider;

public interface OAuthUserInfo {
    String getProviderId();
    String getEmail();
    String getName();
    Provider getProvider();
}

