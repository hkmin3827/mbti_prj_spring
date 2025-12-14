package com.whatslovermbti.mbti_prj.infra.kakao;

import com.whatslovermbti.mbti_prj.infra.kakao.dto.KakaoKeywordResponse;
import jakarta.persistence.Cacheable;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.*;

@Component
@RequiredArgsConstructor
public class KakaoMapClient {

    // WebClientConfig에서 만든 Bean 주입 받기
    private final WebClient kakaoWebClient;

    @Value("${kakao.map.rest-api-key}")
    private String apiKey;

    // infra는 PlaceResDto를 만들지 않는다. 응답 객체만 반환한다.
    public KakaoMapResponse searchNearby(
            double lat,
            double lng,
            int radius,
            String categoryCode
    ) {
        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/category.json")
                        .queryParam("category_group_code", categoryCode)
                        .queryParam("x", lng)
                        .queryParam("y", lat)
                        .queryParam("radius", radius)
                        .queryParam("sort", "distance")
                        .build())
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoMapResponse.class)
                .block();
    }

    // 카테고리별 검색
    public KakaoMapResponse searchNearbyByCategory(
            double lat,
            double lng,
            int radius,
            String categoryCode
    ) {
        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/category.json")
                        .queryParam("category_group_code", categoryCode)
                        .queryParam("x", lng)
                        .queryParam("y", lat)
                        .queryParam("radius", radius)
                        .queryParam("sort", "distance")
                        .build())
                .header("Authorization", "KakaoAK " + apiKey)
                .retrieve()
                .bodyToMono(KakaoMapResponse.class)
                .block();
    }



    private static final String KAKAO_MAP_BASE_URL =
            "https://dapi.kakao.com/v2/local/search/keyword.json";

    private final KakaoMapProperties properties;

    public KakaoKeywordResponse searchByKeyword(String keyword, int page, int size) {
        return kakaoWebClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v2/local/search/keyword.json")
                        .queryParam("query", keyword)
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToMono(KakaoKeywordResponse.class)
                .block();
    }
}

