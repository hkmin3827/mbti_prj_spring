package com.whatslovermbti.mbti_prj.service;
import com.whatslovermbti.mbti_prj.constant.Category;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class MbtiScoreCalculator {

    public int calculateDocumentScore(
            Category category,
            List<String> inferredKeywords,
            Map<String, Integer> keywordWeightMap
    ) {
        int score = 0;

        /* ================= Category 기본 점수 ================= */
        if (category != null) {
            String categoryKey = "__CATEGORY_" + category.name();
            score += keywordWeightMap.getOrDefault(categoryKey, 0);
        }

        /* ================= 키워드 점수 ================= */
        for (String keyword : inferredKeywords) {
            score += keywordWeightMap.getOrDefault(keyword, 0);
        }

        return score;
    }
}
