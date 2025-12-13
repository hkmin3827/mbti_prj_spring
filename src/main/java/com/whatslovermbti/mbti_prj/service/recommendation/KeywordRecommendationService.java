package com.whatslovermbti.mbti_prj.service.recommendation;

import com.whatslovermbti.mbti_prj.constant.ActionType;
import com.whatslovermbti.mbti_prj.constant.MbtiContext;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.User;
import com.whatslovermbti.mbti_prj.repository.UserKeywordActionRepository;
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
    private static final double MBTI_MIN_FLOOR = 5.0;

    private final KeywordBehaviorWeightService behaviorWeightService;
    private final MbtiKeywordWeightService mbtiKeywordWeightService;
    private final UserKeywordActionRepository actionRepository;

    public double calculateKeywordScore(
            User user,
            Keyword keyword,
            MbtiContext context
    ) {
        // 1. 기준 MBTI 결정
        String targetMbti = (context == MbtiContext.SELF)
                ? user.getMbti()
                : user.getPartnerMbti();

        // 2. MBTI 기본 가중치
        int baseWeight =
                mbtiKeywordWeightService.getBaseWeight(targetMbti, keyword);

        boolean isOpposite =
                mbtiKeywordWeightService.isOpposite(targetMbti, keyword);

        // 3. 행동 가중치 계산
        int click = actionRepository.countBy(user, keyword, ActionType.CLICK, context);
        int like  = actionRepository.countBy(user, keyword, ActionType.LIKE, context);
        int save  = actionRepository.countBy(user, keyword, ActionType.SAVE, context);

        double behaviorWeight =
                behaviorWeightService.calculateBehaviorWeight(click, like, save);

        // 4. 희석 적용
        double rawScore =
                mbtiKeywordWeightService.applyDilution(
                        baseWeight,
                        behaviorWeight,
                        isOpposite
                );

        // 5. MBTI 하한선 보장
        return Math.max(rawScore, MBTI_MIN_FLOOR);
    }
}
