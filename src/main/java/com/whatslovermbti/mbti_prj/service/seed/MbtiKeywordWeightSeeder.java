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

    // 해당 mbti 축이 해당 키워드를 얼마나 선호하는가
    @Override
    public void run(String... args) {
        if (seedHistoryRepository.existsById("MBTI_KEYWORD_WEIGHT")) {
            return;
        }

        // 시드 실행

        // ✅ Keyword 한 번만 로딩
        Map<String, Keyword> keywordMap =
                keywordRepository.findAll().stream()
                        .collect(Collectors.toMap(Keyword::getName, k -> k));

        seed(MbtiAxis.I, Map.of(
                "조용한", 25,
                "아늑한", 15,
                "시끌벅적한", -10
        ), keywordMap);
        seed(MbtiAxis.E, Map.of(
                "활동적인", 25,
                "시끌벅적한", 20,
                "조용한", -10
        ), keywordMap);
        seed(MbtiAxis.N, Map.of(
                "감성적인", 20,
                "분위기좋은", 15
        ), keywordMap);

        seed(MbtiAxis.S, Map.of(
                "실용적인", 20,
                "가성비", 15
        ), keywordMap);

        seed(MbtiAxis.F, Map.of(
                "로맨틱한", 20,
                "데이트하기좋은", 15
        ), keywordMap);

        seed(MbtiAxis.T, Map.of(
                "논리적인", 15,
                "깔끔한", 15
        ), keywordMap);

        seed(MbtiAxis.J, Map.of(
                "계획적인", 20,
                "예약가능", 15
        ), keywordMap);

        seed(MbtiAxis.P, Map.of(
                "자유로운", 20,
                "즉흥적인", 15
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
                // 이미 존재하면 무시 (중복 방지)
            }
        }
    }
}
