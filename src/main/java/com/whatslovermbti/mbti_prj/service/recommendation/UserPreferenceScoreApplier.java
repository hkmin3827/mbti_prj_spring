    package com.whatslovermbti.mbti_prj.service.recommendation;

    import org.springframework.stereotype.Component;

    import java.util.List;
    import java.util.Map;

    @Component
    public class UserPreferenceScoreApplier {
        private static final double BONUS_RATIO = 0.3;   // 유저 행동 영향 비율

        private static final double MAX_KEYWORD_BONUS = 50.0;
        private static final double MAX_TOTAL_BONUS = 120.0;
        private static final int TOP_KEYWORD_LIMIT = 3;   // 반영할 상위 키워드 개수
        /** 
         * @param userPrefMap  유저 키워드 선호 Map (이미 로딩됨)
         * @param inferredKeywords 해당 장소의 추론 키워드
         */
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
