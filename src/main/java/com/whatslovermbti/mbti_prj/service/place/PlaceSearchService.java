package com.whatslovermbti.mbti_prj.service.place;
// TODO : JSON 파싱, Place 엔티티 변환, MBTI 가중치 적용

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapClient;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.infra.kakao.dto.KakaoKeywordResponse;
import com.whatslovermbti.mbti_prj.service.recommendation.PlaceRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceSearchService {

    private final PlaceCandidateService placeCandidateService;
    private final PlaceRecommendationService placeRecommendationService;

    /**
     * 통합 검색
     * - 위치는 필수
     * - keyword / category는 선택
     */
    /**
     * 추천 기반 장소 검색
     * - User 기반 (MBTI + 행동 + 선호)
     * - Kakao 후보군은 캐시
     * - 노출은 랜덤 + 가중치
     */
    public KakaoMapResponse search(
            User user,
            double lat,
            double lng,
            int radius,
            String categoryCode,
            int size
    ) {
        // 1️⃣ 후보군 (캐시된 Kakao 결과)
        KakaoMapResponse response =
                placeCandidateService.searchCandidates(
                        user,
                        lat,
                        lng,
                        radius,
                        categoryCode
                );

        // 2️⃣ Kakao → Place 변환은 이미 되어 있다고 가정
        List<KakaoMapResponse.Document> recommended =
                placeRecommendationService.recommend(
                        user,
                        response.getDocuments(),
                        size
                );

        response.applyDocuments(recommended);
        return response;
    }
}