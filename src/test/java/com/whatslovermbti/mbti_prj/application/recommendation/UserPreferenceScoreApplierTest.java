package com.whatslovermbti.mbti_prj.application.recommendation;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class UserPreferenceScoreApplierTest {

    private final UserPreferenceScoreApplier applier =
            new UserPreferenceScoreApplier();


    @Test
    void 유저_선호_키워드가_장소_키워드와_겹치면_보너스가_부여된다() {
        Map<String, Double> userPrefMap = Map.of(
                "조용한", 40.0,
                "분위기좋은", 30.0,
                "감성", 10.0
        );

        List<String> inferredKeywords = List.of(
                "조용한",
                "감성",
                "힙한"
        );

        double bonus = applier.apply(userPrefMap, inferredKeywords);

        assertThat(bonus).isGreaterThan(0);
    }

    @Test
    void 상위_키워드_3개만_반영된다() {
        Map<String, Double> userPrefMap = Map.of(
                "A", 100.0,
                "B", 90.0,
                "C", 80.0,
                "D", 70.0
        );

        List<String> inferredKeywords = List.of("A", "B", "C", "D");

        double bonus = applier.apply(userPrefMap, inferredKeywords);

        assertThat(bonus).isLessThan(200);
    }

    @Test
    void 최대_총_보너스를_초과할_수_없다() {
        Map<String, Double> userPrefMap = Map.of(
                "A", 1000.0,
                "B", 1000.0,
                "C", 1000.0
        );

        List<String> inferredKeywords = List.of("A", "B", "C");

        double bonus = applier.apply(userPrefMap, inferredKeywords);

        assertThat(bonus).isEqualTo(120.0);
    }
}
