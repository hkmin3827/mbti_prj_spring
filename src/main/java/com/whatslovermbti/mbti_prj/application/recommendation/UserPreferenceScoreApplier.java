package com.whatslovermbti.mbti_prj.application.recommendation;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class UserPreferenceScoreApplier {
    private static final double BONUS_RATIO = 0.7;

    private static final double MAX_KEYWORD_BONUS = 40.0;
    private static final double MAX_TOTAL_BONUS = 120.0;
    private static final int TOP_KEYWORD_LIMIT = 3;

    public double apply(
            Map<String, Double> userPrefMap,
            List<String> inferredKeywords
    ) {
        if (userPrefMap.isEmpty() || inferredKeywords.isEmpty()) {
            return 0.0;
        }

        double bonus =
                userPrefMap.entrySet().stream()
                        .filter(e -> inferredKeywords.contains(e.getKey()))
                        .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                        .limit(TOP_KEYWORD_LIMIT)
                        .mapToDouble(e -> {
                            double raw = e.getValue() * BONUS_RATIO;
                            return Math.min(raw, MAX_KEYWORD_BONUS);
                        })
                        .sum();

        return Math.min(bonus, MAX_TOTAL_BONUS);
    }
}
