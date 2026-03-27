package com.whatslovermbti.mbti_prj.global.security.oauth.userInfo;

import com.whatslovermbti.mbti_prj.global.constant.Provider;

import java.util.Map;

public class GoogleOAuthUserInfo implements OAuthUserInfo {
    private final Map<String, Object> attributes;

    public GoogleOAuthUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public Provider getProvider() {
        return Provider.GOOGLE;
    }
}