package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.dto.place.PlaceResDto;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
import com.whatslovermbti.mbti_prj.service.place.PlaceCandidateService;
import com.whatslovermbti.mbti_prj.util.RandomPicker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

// 유저에게 이 후보군 중 무엇을, 어떤 비율로 보여줄 것인가
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceRecommendationService {

    private static final int TOP_N = 20;
    private static final int RANDOM_COUNT = 5;

    private final KeywordRecommendationService keywordRecommendationService;
    private final PlaceCandidateService placeCandidateService;

    /**
     * ⭐ 핵심 추천 엔진
     */
    public List<KakaoMapResponse.Document> recommendFromCandidates(
            User user,
            List<KakaoMapResponse.Document> candidates,
            int limit
    ) {
        if (candidates == null || candidates.isEmpty()) {
            return List.of();
        }

        List<KakaoMapResponse.Document> sorted =
                candidates.stream()
                        .sorted(Comparator.comparingDouble(
                                (KakaoMapResponse.Document doc) -> calculateDocumentScore(user, doc)
                        ).reversed())
                        .toList();

        return RandomPicker.pickTopWithRandom(
                sorted,
                TOP_N,
                RANDOM_COUNT,
                limit
        );
    }

    private double calculateDocumentScore(
            User user,
            KakaoMapResponse.Document doc
    ) {
        // TODO: MBTI / Keyword / User Action 반영
        return 1.0;
    }
}
