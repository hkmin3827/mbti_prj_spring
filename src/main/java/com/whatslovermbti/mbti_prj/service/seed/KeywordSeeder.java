package com.whatslovermbti.mbti_prj.service.seed;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.AppSeedHistory;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import com.whatslovermbti.mbti_prj.repository.SeedHistoryRepository;
import com.whatslovermbti.mbti_prj.service.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1) // MbtiKeywordSeeder보다 먼저 실행
public class KeywordSeeder implements CommandLineRunner {


    private final KeywordRepository keywordRepository;
    private final SeedHistoryRepository seedHistoryRepository;
    private final KeywordService keywordService;

    @Override
    public void run(String... args) {
        if (seedHistoryRepository.existsById("KEYWORD")) {
            return;
        }

        keywordService.createKeywordWithNormalizations(
                "조용한", MbtiAxis.I,
                List.of("조용한", "조용")
        );

        keywordService.createKeywordWithNormalizations(
                "아늑한", MbtiAxis.I,
                List.of("아늑한", "아늑")
        );

        keywordService.createKeywordWithNormalizations(
                "시끌벅적한", MbtiAxis.E,
                List.of("시끌벅적한", "시끄러운", "활기찬")
        );

        keywordService.createKeywordWithNormalizations(
                "활동적인", MbtiAxis.E,
                List.of("활동적인", "활동적", "활동")
        );

        keywordService.createKeywordWithNormalizations(
                "감성적인", MbtiAxis.N,
                List.of("감성적인", "감성적", "감성")
        );

        keywordService.createKeywordWithNormalizations(
                "분위기좋은", MbtiAxis.N,
                List.of("분위기좋은", "분위기 좋은")
        );

        keywordService.createKeywordWithNormalizations(
                "실용적인", MbtiAxis.S,
                List.of("실용적인", "실용적", "실용")
        );

        keywordService.createKeywordWithNormalizations(
                "가성비", MbtiAxis.S,
                List.of("가성비", "가성 비", "저렴한", "저렴")
        );

        keywordService.createKeywordWithNormalizations(
                "로맨틱한", MbtiAxis.F,
                List.of("로맨틱한", "로맨틱")
        );

        keywordService.createKeywordWithNormalizations(
                "데이트하기좋은", MbtiAxis.F,
                List.of("데이트", "데이트하기 좋은", "데이트하기좋은")
        );

        keywordService.createKeywordWithNormalizations(
                "논리적인", MbtiAxis.T,
                List.of("논리적인", "논리적", "논리", "지적인")
        );

        keywordService.createKeywordWithNormalizations(
                "깔끔한", MbtiAxis.T,
                List.of("깔끔한", "깔끔", "심플한")
        );

        keywordService.createKeywordWithNormalizations(
                "계획적인", MbtiAxis.J,
                List.of("계획적인", "계획적", "계획")
        );

        keywordService.createKeywordWithNormalizations(
                "예약가능", MbtiAxis.J,
                List.of("예약가능", "예약 가능", "예약")
        );

        keywordService.createKeywordWithNormalizations(
                "즉흥적인", MbtiAxis.P,
                List.of("즉흥적인", "즉흥적", "즉흥")
        );

        keywordService.createKeywordWithNormalizations(
                "자유로운", MbtiAxis.P,
                List.of("자유로운", "자유", "프리한")
        );


        seedHistoryRepository.save(new AppSeedHistory("KEYWORD"));
    }

    private void seed(String name, MbtiAxis axis) {
        if (keywordRepository.existsByName(name)) return;

        Keyword k = new Keyword(name, axis);
        keywordRepository.save(k);
    }
}
