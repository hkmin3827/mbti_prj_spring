package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.entity.UserKeywordPreference;
import com.whatslovermbti.mbti_prj.repository.UserKeywordActionRepository;
import com.whatslovermbti.mbti_prj.repository.UserKeywordPreferenceRepository;
import com.whatslovermbti.mbti_prj.service.weight.KeywordBehaviorWeightService;
import com.whatslovermbti.mbti_prj.service.weight.MbtiKeywordWeightService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 추천 조합 서비스 User + Keyword + MbtiContext → 최종 키워드 점수
//

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class KeywordRecommendationService {

    private static final double MBTI_MIN_FLOOR = 1.5;

    private final MbtiKeywordWeightService mbtiKeywordWeightService;
    private final UserKeywordPreferenceRepository preferenceRepository;

    /**
     * 유저 + 키워드 → 최종 점수
     */
    public double calculateKeywordScore(
            User user,
            Keyword keyword
    ) {
        String targetMbti =
                user.getPartnerMbti() != null
                        ? user.getPartnerMbti()
                        : user.getMbti();

        // 1️⃣ MBTI 기본 가중치
        int baseWeight =
                mbtiKeywordWeightService.getBaseWeight(
                        targetMbti,
                        keyword
                );

        boolean isOpposite =
                mbtiKeywordWeightService.isOpposite(
                        targetMbti,
                        keyword
                );

        // 2️⃣ 유저 누적 선호
        int userPreference =
                preferenceRepository.findByUserAndKeyword(user, keyword)
                        .map(UserKeywordPreference::getScore)
                        .orElse(0);

        // 3️⃣ AI 보정 (지금은 0, 추후 주입)
        double aiBoost = 0.0;

        double rawScore =
                mbtiKeywordWeightService.applyDilution(
                        baseWeight + userPreference + aiBoost,
                        0.0,
                        isOpposite
                );

        return Math.max(rawScore, MBTI_MIN_FLOOR);
    }
}