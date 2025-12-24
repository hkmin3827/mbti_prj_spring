package com.whatslovermbti.mbti_prj.service.seed;

import com.whatslovermbti.mbti_prj.constant.MbtiAxis;
import com.whatslovermbti.mbti_prj.entity.AppSeedHistory;
import com.whatslovermbti.mbti_prj.entity.Keyword;
import com.whatslovermbti.mbti_prj.repository.KeywordRepository;
import com.whatslovermbti.mbti_prj.repository.SeedHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

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

        seed("조용한", MbtiAxis.I);
        seed("아늑한", MbtiAxis.I);

        seed("시끌벅적한", MbtiAxis.E);
        seed("활동적인", MbtiAxis.E);

        seed("감성적인", MbtiAxis.N);
        seed("분위기좋은", MbtiAxis.N);

        seed("실용적인", MbtiAxis.S);
        seed("가성비", MbtiAxis.S);

        seed("로맨틱한", MbtiAxis.F);
        seed("데이트", MbtiAxis.F);

        seed("논리적인", MbtiAxis.T);
        seed("깔끔한", MbtiAxis.T);

        seed("계획적인", MbtiAxis.J);
        seed("예약가능", MbtiAxis.J);

        seed("즉흥적인", MbtiAxis.P);
        seed("자유로운", MbtiAxis.P);

        seedHistoryRepository.save(new AppSeedHistory("KEYWORD"));
    }

    private void seed(String name, MbtiAxis axis) {
        if (keywordRepository.existsByName(name)) return;

        Keyword k = new Keyword();
        k.setName(name);
        k.setAxis(axis);
        keywordRepository.save(k);
    }
}
