package com.whatslovermbti.mbti_prj.service.weight;

import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.MbtiKeywordWeight;
import com.whatslovermbti.mbti_prj.repository.MbtiKeywordWeightRepository;
import com.whatslovermbti.mbti_prj.util.MbtiAxisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MbtiKeywordWeightService {

    private static final double DILUTION_RATIO = 0.4;

    private final MbtiKeywordWeightRepository repository;

    /**
     * MBTI + Keyword → 반대 성향 여부
     */
    public boolean isOpposite(String mbti, Keyword keyword) {
        return getBaseWeight(mbti, keyword) < 0;
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
