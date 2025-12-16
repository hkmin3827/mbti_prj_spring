package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordPreference;
import com.whatslovermbti.mbti_prj.repository.UserKeywordPreferenceRepository;
import com.whatslovermbti.mbti_prj.service.KeywordWeightAggregator;
import com.whatslovermbti.mbti_prj.service.weight.MbtiKeywordWeightPolicy;
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
    private final MbtiKeywordWeightPolicy policy;

    /**
     * 유저 + 키워드 → 최종 추천 점수
     */
    public double calculateKeywordScore(User user, Keyword keyword) {

        String targetMbti =
                user.getPartnerMbti() != null
                        ? user.getPartnerMbti()
                        : user.getMbti();

        Map<Long, Integer> mbtiWeightMap =
                aggregator.getMbtiWeightMap(targetMbti);

        Map<Long, Integer> userPrefMap =
                aggregator.getUserPreferenceMap(user);

        int mbtiWeight =
                mbtiWeightMap.getOrDefault(keyword.getId(), 0);

        int userPreference =
                userPrefMap.getOrDefault(keyword.getId(), 0);

        boolean isOpposite =
                policy.isOpposite(mbtiWeight);

        // ✅ AI 보정 훅 (지금은 0, 추후 모델 연결)
        double aiBoost = getAiBoost(user, keyword);

        double score =
                policy.applyDilution(
                        mbtiWeight,
                        userPreference,
                        isOpposite
                )  + aiBoost;


        // ^^^^^ mbti별로 바뀌는 지 확인용 로그
        if (mbtiWeight != 0 || userPreference != 0) {
            log.info(
                    "[KEYWORD_SCORE] mbti={}, keyword={}, mbtiWeight={}, userPref={}, aiBoost={}, final={}",
                    targetMbti,
                    keyword.getName(),
                    mbtiWeight,
                    userPreference,
                    aiBoost,
                    score
            );
        }
        // ^^^^^ 여기까지

        return Math.max(score, MBTI_MIN_FLOOR);
    }

    private double getAiBoost(User user, Keyword keyword) {
        return 0.0;
    }
}