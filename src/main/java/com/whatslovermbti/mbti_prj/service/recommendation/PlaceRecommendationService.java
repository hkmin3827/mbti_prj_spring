package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.infra.kakao.KakaoMapResponse;
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

    public List<KakaoMapResponse.Document> recommend(
            User user,
            List<KakaoMapResponse.Document> candidates,
            int limit
    ) {
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
        // ⚠️ 아직 Place/Keyword 매핑 전이므로
        // 지금은 "기본 점수"만 두거나
        // categoryName, placeName 기반 임시 점수 가능

        return 1.0; // ← 지금 단계에선 이게 정상
    }
}
