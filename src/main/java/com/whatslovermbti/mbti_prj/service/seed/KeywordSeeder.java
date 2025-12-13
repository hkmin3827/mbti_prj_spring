package com.whatslovermbti.mbti_prj.service.seed;

import com.whatslovermbti.mbti_prj.entity.AppSeedHistory;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import com.whatslovermbti.mbti_prj.repository.SeedHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Order(1) // MbtiKeywordSeeder보다 먼저 실행
public class KeywordSeeder implements CommandLineRunner {

    private final KeywordRepository keywordRepository;
    private final SeedHistoryRepository seedHistoryRepository;

    @Override
    public void run(String... args) {
        if (seedHistoryRepository.existsById("KEYWORD")) {
            return;
        }

        List<String> keywords = List.of(
                "조용한",
                "아늑한",
                "시끌벅적한",
                "활동적인",
                "감성적인",
                "분위기좋은",
                "실용적인",
                "가성비",
                "로맨틱한",
                "데이트",
                "논리적인",
                "깔끔한",
                "계획적인",
                "예약가능",
                "즉흥적인",
                "자유로운"
        );



        // ✅ 이미 존재하는 키워드 이름을 한 번에 조회
        Set<String> existingNames = keywordRepository.findAll().stream()
                .map(Keyword::getName)
                .collect(Collectors.toSet());

        // ✅ 없는 것만 insert
        for (String name : keywords) {
            if (!existingNames.contains(name)) {
                Keyword keyword = new Keyword();
                keyword.setName(name);
                keywordRepository.save(keyword);
            }
        }

        seedHistoryRepository.save(new AppSeedHistory("KEYWORD"));
    }
}
