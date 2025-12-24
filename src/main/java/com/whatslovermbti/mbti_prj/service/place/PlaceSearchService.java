package com.whatslovermbti.mbti_prj.service.place;
// TODO : JSON 파싱, Place 엔티티 변환, MBTI 가중치 적용

import com.whatslovermbti.mbti_prj.constant.Category;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.service.recommendation.PlaceRecommendationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlaceSearchService {
    private final PlaceCandidateService placeCandidateService;
    private final PlaceRecommendationService placeRecommendationService;

    /**
     * 통합 검색 진입점 (오케스트레이터)
     * 1. Kakao 후보군 확보
     * 2. Document → Place 변환 (DB write)
     * 3. 추천 엔진 호출 (순수 계산)
     */
    public KakaoMapResponse search(
            User user,
            MbtiContext context,
            double lat,
            double lng,
            int radius,
            Category category,
            int size
    ) {

        // 후보군 (캐시된 Kakao 결과)
        List<KakaoMapResponse.Document> candidates =
                placeCandidateService.fetchCandidates(
                        lat,
                        lng,
                        radius,
                        category
                );

        // Kakao → Place 변환은 이미 되어 있다고 가정
        List<KakaoMapResponse.Document> picked =
                placeRecommendationService.recommendFromCandidates(
                        user,
                        context,
                        candidates,
                        size
                );

        KakaoMapResponse response = new KakaoMapResponse();
        response.applyDocuments(picked);
        return response;
    }
}