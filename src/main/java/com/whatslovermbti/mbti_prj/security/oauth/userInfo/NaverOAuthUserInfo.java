package com.whatslovermbti.mbti_prj.security.oauth.userInfo;

import com.whatslovermbti.mbti_prj.constant.Provider;
import java.util.Map;

public class NaverOAuthUserInfo implements OAuthUserInfo{
    private final Map<String, Object> attributes;

    public NaverOAuthUserInfo(Map<String, Object> attributes) {
        Object response = attributes.get("response");

        if (!(response instanceof Map)) {
            throw new IllegalArgumentException("Invalid Naver OAuth response");
        }

        @SuppressWarnings("unchecked")
        Map<String, Object> castedResponse = (Map<String, Object>) response;
        this.attributes = castedResponse;
    }
    @Override
    public Provider getProvider() {
        return Provider.NAVER;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
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