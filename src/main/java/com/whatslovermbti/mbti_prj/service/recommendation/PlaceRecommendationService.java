package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.Place;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.repository.PlaceRepository;
import com.whatslovermbti.mbti_prj.service.weight.MbtiKeywordWeightService;
import com.whatslovermbti.mbti_prj.util.RandomPicker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceRecommendationService {
    private static final int TOP_N = 10;
    private static final int RANDOM_COUNT = 5;

    private final PlaceRepository placeRepository;
    private final KeywordRecommendationService keywordRecommendationService;
    private final MbtiKeywordWeightService mbtiKeywordWeightService;

    // 정렬을 위한 내부 클래스
    private record PlaceScore(Place place, double score) {}
    /**
     * 장소 추천 메인 진입점
     */
    public List<Place> recommendPlaces(
            User user,
            MbtiContext context,
            int limit
    ) {
        List<PlaceScore> scoredPlaces =
                placeRepository.findAll().stream()
                        .map(place -> new PlaceScore(
                                place,
                                calculatePlaceScore(user, place, context)
                        ))
                        .sorted(Comparator.comparingDouble(PlaceScore::score).reversed())
                        .collect(Collectors.toList());

        // 1. Place만 추출 (정렬된 상태)
        List<Place> sortedPlaces = scoredPlaces.stream()
                .map(PlaceScore::place)
                .collect(Collectors.toList());

        // 2. 랜덤 섞기 적용
        List<Place> mixed =
                RandomPicker.pickTopWithRandom(sortedPlaces, TOP_N, RANDOM_COUNT);

        // 3. limit 적용
        return mixed.stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    private double calculatePlaceScore(
            User user,
            Place place,
            MbtiContext context
    ) {
        return place.getKeywords().stream()
                .mapToDouble(keyword ->
                        keywordRecommendationService.calculateKeywordScore(
                                user,
                                keyword,
                                context
                        )
                )
                .sum();
    }
}
