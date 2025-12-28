package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.service.keyword.KeywordWeightAggregator;
import com.whatslovermbti.mbti_prj.service.policy.BehaviorDilutionPolicy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

// 추천 조합 서비스 User + Keyword + MbtiContext → 최종 키워드 점수

@Slf4j

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordRecommendationService {

    private static final double MBTI_MIN_FLOOR = 1.5;

    private final KeywordWeightAggregator aggregator;
    private final BehaviorDilutionPolicy policy;

    /**
     * 유저 + 키워드 → 최종 추천 점수
     */
    public double calculateKeywordScore(User user, String mbti, Keyword keyword) {

        Map<Long, Integer> mbtiWeightMap =
                aggregator.getMbtiKeywordWeightMapById(mbti);

        Map<Long, Integer> userPrefMap =
                aggregator.getUserKeywordPreferenceMapById(user);

        int mbtiWeight =
                mbtiWeightMap.getOrDefault(keyword.getId(), 0);

        int userPreference =
                userPrefMap.getOrDefault(keyword.getId(), 0);

        boolean isOpposite =
                policy.isOpposite(mbtiWeight);


        double score =
                policy.applyDilution(
                        mbtiWeight,
                        userPreference,
                        isOpposite
                );


        // ^^^^^ mbti별로 바뀌는 지 확인용 로그
        if (mbtiWeight != 0 || userPreference != 0) {
            log.info(
                    "[KEYWORD_SCORE] mbti={}, keyword={}, mbtiWeight={}, userPref={}, final={}",
                    mbti,
                    keyword.getName(),
                    mbtiWeight,
                    userPreference,
                    score
            );
        }
        // ^^^^^ 여기까지

        return Math.max(score, MBTI_MIN_FLOOR);
    }
}