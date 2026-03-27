package com.whatslovermbti.mbti_prj.application.placeKeywordRefiner;

import com.whatslovermbti.mbti_prj.domain.keyword.entity.Keyword;
import com.whatslovermbti.mbti_prj.application.keyword.KeywordNormalizationService;
import com.whatslovermbti.mbti_prj.domain.placeKeywordRefiner.dto.PlaceAtmosphereResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
public class PlaceKeywordAdjustmentCalculator {

    private static final int ATMOSPHERE_BOOST = 2;
    private final KeywordNormalizationService normalizationService;

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

