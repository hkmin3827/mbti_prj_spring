package com.whatslovermbti.mbti_prj.domain.recommendation.dto;

import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;

import java.util.List;

public record RecommendResponse(
        List<KakaoMapResponse.Document> allPlaces,
        List<KakaoMapResponse.Document> pagePlaces,
        int page,
        int size,
        int totalElements
) {}