package com.whatslovermbti.mbti_prj.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient kakaoWebClient(
            WebClient.Builder builder,
            @Value("${kakao.map.rest-api-key}") String apiKey
    ) {
        return builder
                .baseUrl("https://dapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + apiKey)
                .build();
    }
}