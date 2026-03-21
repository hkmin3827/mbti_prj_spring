package com.whatslovermbti.mbti_prj.service.seed;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.AppSeedHistory;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.entity.MbtiKeywordWeight;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import com.whatslovermbti.mbti_prj.repository.MbtiKeywordWeightRepository;
import com.whatslovermbti.mbti_prj.repository.SeedHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(2)
public class MbtiKeywordWeightSeeder implements CommandLineRunner {

    private final KeywordRepository keywordRepository;
    private final MbtiKeywordWeightRepository weightRepository;
    private final SeedHistoryRepository seedHistoryRepository;

    @Override
    public void run(String... args) {
        if (seedHistoryRepository.existsById("MBTI_KEYWORD_WEIGHT")) {
            return;
        }

        Map<String, Keyword> keywordMap =
                keywordRepository.findAll().stream()
                        .collect(Collectors.toMap(Keyword::getName, k -> k));


        /* ================= I / E ================= */

        seed(MbtiAxis.I, Map.of(
                "조용한", 10,
                "아늑한", 8,
                "프라이빗한", 6,
                "활동적인", -4,
                "화려한", -5
        ), keywordMap);

        seed(MbtiAxis.E, Map.of(
                "활동적인", 10,
                "화려한", 8,
                "힙한", 6,
                "조용한", -4,
                "프라이빗한", -3
        ), keywordMap);

        /* ================= N / S ================= */

        seed(MbtiAxis.N, Map.of(
                "감성적인", 10,
                "분위기좋은", 8,
                "감각적인", 6,
                "실용적인", -4,
                "가성비좋은", -3
        ), keywordMap);

        seed(MbtiAxis.S, Map.of(
                "실용적인", 10,
                "가성비좋은", 8,
                "정갈한", 6,
                "감성적인", -4,
                "감각적인", -3
        ), keywordMap);

        /* ================= F / T ================= */

        seed(MbtiAxis.F, Map.of(
                "로맨틱한", 10,
                "따뜻한", 8,
                "논리적인", -4,
                "깔끔한", -3
        ), keywordMap);

        seed(MbtiAxis.T, Map.of(
                "논리적인", 10,
                "깔끔한", 8,
                "로맨틱한", -4,
                "따뜻한", -3
        ), keywordMap);

        /* ================= J / P ================= */

        seed(MbtiAxis.J, Map.of(
                "계획적인", 10,
                "예약가능", 8,
                "즉흥적인", -5,
                "자유로운", -3
        ), keywordMap);

        seed(MbtiAxis.P, Map.of(
                "즉흥적인", 10,
                "자유로운", 8,
                "계획적인", -5,
                "예약가능", -3
        ), keywordMap);

        seedHistoryRepository.save(new AppSeedHistory("MBTI_KEYWORD_WEIGHT"));
    }

    private void seed(
            MbtiAxis axis,
            Map<String, Integer> keywordWeights,
            Map<String, Keyword> keywordMap
    ) {
            for (Map.Entry<String, Integer> entry : keywordWeights.entrySet()) {

            String keywordName = entry.getKey();
            int weight = entry.getValue();

            Keyword keyword = keywordMap.get(keywordName);
            if (keyword == null) {
                throw new IllegalStateException(
                        "Keyword 먼저 생성되어야 합니다: " + keywordName
                );
            }

            MbtiKeywordWeight mw = new MbtiKeywordWeight();
            mw.setMbtiAxis(axis);
            mw.setKeyword(keyword);
            mw.setWeight(weight);

            try {
                weightRepository.save(mw);
            } catch (DataIntegrityViolationException e) {
            }
        }
    }
}
