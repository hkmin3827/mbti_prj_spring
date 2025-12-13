package com.whatslovermbti.mbti_prj.service.weight;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// 단순 계산 로직
@Service
@RequiredArgsConstructor
public class KeywordBehaviorWeightService {

    private static final double CLICK_A = 1.2;
    private static final double LIKE_A  = 2.5;
    private static final double SAVE_A  = 4.0;

    private static final double BEHAVIOR_CAP = 15.0;

    public double calculateBehaviorWeight(int click, int like, int save) {
        double score =
                logWeight(click, CLICK_A)
                        + logWeight(like, LIKE_A)
                        + logWeight(save, SAVE_A);

        return Math.min(score, BEHAVIOR_CAP);
    }

    private double logWeight(int count, double a) {
        return a * Math.log(1 + count);
    }
}
