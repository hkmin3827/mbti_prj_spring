package com.whatslovermbti.mbti_prj.infra.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoUnlinkClient {

    private final WebClient kakaoAuthWebClient;

    public void unlink(String kakaoUserId) {

        kakaoAuthWebClient.post()
                .uri("/v1/user/unlink")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("target_id_type=user_id&target_id=" + kakaoUserId)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}