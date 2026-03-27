package com.whatslovermbti.mbti_prj.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient kakaoAuthWebClient(
            WebClient.Builder builder,
            @Value("${kakao.admin-key}") String adminKey
    ) {
        return builder
                .baseUrl("https://kapi.kakao.com")
                .defaultHeader("Authorization", "KakaoAK " + adminKey)
                .build();
    }

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

    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}