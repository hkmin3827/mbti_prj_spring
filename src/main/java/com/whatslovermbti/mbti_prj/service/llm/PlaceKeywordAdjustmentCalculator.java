package com.whatslovermbti.mbti_prj.service.llm;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.service.keyword.KeywordNormalizationService;
import com.whatslovermbti.mbti_prj.service.llm.dto.PlaceAtmosphereResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PlaceKeywordAdjustmentCalculator {
    /** AI 키워드 1개당 기본 보정치 */
    // AI가 주는 raw Keyword가 중복으로 정규화시 동일 키워드가 되면 1번만 반영
    private static final int ATMOSPHERE_BOOST = 2;

    private final KeywordNormalizationService normalizationService;

    /**
     * 분위기 키워드 → 표준 Keyword → 가중치 보정 Map
     */
    public Map<Keyword, Integer> calculate(
            PlaceAtmosphereResult atmosphere
    ) {
        Map<Keyword, Integer> adjustment = new HashMap<>();
        Set<Keyword> applied = new HashSet<>();


        for (String rawKeyword : atmosphere.rawKeywords()) {
            Keyword standard;
            try {
                standard = normalizationService.normalize(rawKeyword);
            } catch (Exception e) {
                // 등록 안 된 키워드는 스킵
                continue;
            }

            if (applied.contains(standard)) {
                continue;
            }

            applied.add(standard);
            adjustment.put(standard, ATMOSPHERE_BOOST);
            }

        return adjustment;
    }
}

