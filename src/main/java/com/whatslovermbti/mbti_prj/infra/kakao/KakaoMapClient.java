package com.whatslovermbti.mbti_prj.infra.kakao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class KakaoMapClient {

    private final WebClient kakaoWebClient;

    @Value("${kakao.map.rest-api-key}")
    private String apiKey;

    public boolean existsPlace(String kakaoPlaceId) {
        try {
            kakaoWebClient.get()
                    .uri("/v2/local/search/keyword.json?query=" + kakaoPlaceId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public KakaoMapResponse searchByCategory(
            double lat,
            double lng,
            int radius,
            int page,
            String categoryCode
    ) {
        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/category.json")
                        .queryParam("category_group_code", categoryCode)
                        .queryParam("x", lng)
                        .queryParam("y", lat)
                        .queryParam("radius", radius)
                        .queryParam("page", page)
                        .queryParam("sort", "distance")
                        .build())
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoMapResponse.class)
                .block();
    }


    public KakaoMapResponse searchByKeywordWithLocation(
            String keyword,
            double lat,
            double lng,
            int radius,
            int page,
            int size
    ) {
        if (keyword == null || keyword.isBlank()) {
            throw new IllegalArgumentException("Kakao keyword search requires non-empty keyword");
        }

        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", keyword)
                        .queryParam("x", lng)
                        .queryParam("y", lat)
                        .queryParam("radius", radius)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build()
                )
                .retrieve()
                .bodyToMono(KakaoMapResponse.class)
                .block();
    }
}

