package com.whatslovermbti.mbti_prj.service.llm;

import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.service.keyword.KeywordNormalizationService;
import com.whatslovermbti.mbti_prj.service.llm.dto.PlaceAtmosphereResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class PlaceKeywordAdjustmentCalculator {

    private static final int ATMOSPHERE_BOOST = 2;

    private final KeywordNormalizationService normalizationService;

    /**
     * 분위기 키워드 → 표준 Keyword → 가중치 보정 Map
     */
    public Map<Keyword, Integer> calculate(
            PlaceAtmosphereResult atmosphere
    ) {
        Map<Keyword, Integer> adjustment = new HashMap<>();

        for (String rawKeyword : atmosphere.rawKeywords()) {

            Keyword standard;

            try {
                standard = normalizationService.normalize(rawKeyword);
            } catch (Exception e) {
                // ❗ 등록 안 된 키워드는 조용히 스킵
                continue;
            }

            adjustment.merge(
                    standard,
                    ATMOSPHERE_BOOST,
                    Integer::sum
            );
        }

        return adjustment;
    }
}

