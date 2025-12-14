package com.whatslovermbti.mbti_prj.security.oauth.userInfo;

import com.whatslovermbti.mbti_prj.constant.Provider;

import java.util.Map;

public class NaverOAuthUserInfo implements OAuthUserInfo{
    private final Map<String, Object> attributes;

    public NaverOAuthUserInfo(Map<String, Object> attributes) {
        this.attributes = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public Provider getProvider() {
        return Provider.NAVER;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString(); // ✅ 네이버 고유 ID
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
