package com.whatslovermbti.mbti_prj.dto.recommend;

import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;

import java.util.List;

public record RecommendResponse(
        List<KakaoMapResponse.Document> allPlaces,
        List<KakaoMapResponse.Document> pagePlaces,
        int page,
        int size,
        int totalElements
) {}