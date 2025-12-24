package com.whatslovermbti.mbti_prj.service.policy;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BehaviorDilutionPolicy {

    private static final double DILUTION_RATIO = 0.4;

    /**
     * MBTI + Keyword → 반대 성향 여부
     */
    public boolean isOpposite(int mbtiWeight) {
        return mbtiWeight < 0;
    }

    /**
     * 행동 가중치에 따른 희석 정책
     */
    public double applyDilution(
            double mbtiWeight,
            double behaviorWeight,
            boolean isOpposite
    ) {
        if (!isOpposite) {
            return mbtiWeight + behaviorWeight;
        }

        return mbtiWeight - (behaviorWeight * DILUTION_RATIO);
    }
}
