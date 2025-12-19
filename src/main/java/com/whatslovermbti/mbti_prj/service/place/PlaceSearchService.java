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

    private static final int DEFAULT_RADIUS = 1500;

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
            String mbti,
            double lat,
            double lng,
            String categoryCode,
            int size
    ) {

        // 1️⃣ 후보군 (캐시된 Kakao 결과)
        List<KakaoMapResponse.Document> candidates =
                placeCandidateService.searchCandidates(
                        user,
                        mbti,
                        lat,
                        lng,
                        DEFAULT_RADIUS,
                        categoryCode
                );

        // 2️⃣ Kakao → Place 변환은 이미 되어 있다고 가정
        List<KakaoMapResponse.Document> picked =
                placeRecommendationService.recommendFromCandidates(
                        user,
                        candidates,
                        size
                );

        KakaoMapResponse response = new KakaoMapResponse();
        response.applyDocuments(picked);
        return response;
    }
}