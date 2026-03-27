package com.whatslovermbti.mbti_prj.application.recommendation;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class MbtiScoreCalculator {
    public double calculateDocumentScore(
            List<String> inferredKeywords,
            Map<String, Double> keywordWeightMap
    ) {
        double score = 0.0;

        for (String keyword : inferredKeywords) {
            score += keywordWeightMap.getOrDefault(keyword, 0.0);
        }

        return score;
    }
}
